package com.pdianalyzer.service;

import com.pdianalyzer.domain.model.Colaborador;
import com.pdianalyzer.domain.model.MetricasDesempenho;
import com.pdianalyzer.domain.model.Nivel;
import com.pdianalyzer.domain.repository.ColaboradorRepositoryJpa;
import com.pdianalyzer.exception.ItemNotFoundException;
import com.pdianalyzer.exception.PDIRelatorioException;
import com.smarthirepro.core.service.IAvaliacaoLlm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PlanoPromptService implements IAvaliacaoLlm {

  private final ColaboradorRepositoryJpa colaboradorRepository;

  @Override
  public String avaliacaoPrompt(UUID colaboradorId, UUID proximoNivelId) {
    Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
      .orElseThrow(() -> new ItemNotFoundException("Colaborador", colaboradorId));

    MetricasDesempenho metricas = colaborador.getMetricasDesempenho();
    if (metricas == null) {
      throw new PDIRelatorioException("Colaborador não possui métricas de desempenho para análise.");
    }

    Nivel nivelAtual = colaborador.getCargo().getNivel();
    Nivel proximoNivel = nivelAtual.getProximoNivel();

    if (proximoNivel == null) {
      throw new PDIRelatorioException("Colaborador já está no nível máximo da trilha.");
    }

    String competenciasRequeridas = String.format(
      "Competências Técnicas: %s\nCompetências Pessoais: %s",
      proximoNivel.getCargoCompetencias().getCompetenciasTecnicas(),
      proximoNivel.getCargoCompetencias().getCompetenciasPessoais()
    );

    String competenciasAtuais = String.format(
      "Competências Técnicas: %s\nCompetências Pessoais: %s",
      metricas.getCompetenciasTecnicas(),
      metricas.getCompetenciasPessoais()
    );

    String prompt = String.format(
      """
              Aja como um especialista sênior em Liderança e Desenvolvimento (L&D) e um coach de carreira executivo. Sua tarefa é analisar o perfil de um colaborador e as expectativas para sua próxima posição para criar um Plano de Desenvolvimento Individual (PDI) estratégico, detalhado e acionável.

              **Contexto Estratégico:**
              - **Colaborador:** %s
              - **Nível Atual:** %s
              - **Nível Alvo (Próxima Posição):** %s

              **Situação Atual do Colaborador (Métricas de Desempenho):**
              %s

              **Expectativas para o Nível Alvo (%s):**
              %s

              **Instruções de Saída OBRIGATÓRIAS:**
              Responda EXCLUSIVAMENTE com um objeto JSON válido. O objeto JSON deve conter os seguintes campos-chave, preenchidos de forma inteligente e profissional:
              1. "objetivo": (String) Defina um objetivo de desenvolvimento principal no formato SMART (Específico, Mensurável, Atingível, Relevante, Temporal). Exemplo: "Desenvolver autonomia técnica e habilidades de mentoria para estar apto à promoção para Desenvolvedor Pleno nos próximos 6 meses."
              2. "acao": (String) Descreva a principal ação prática ou projeto que o colaborador deve focar para atingir o objetivo. Exemplo: "Assumir a liderança técnica da nova funcionalidade de pagamentos, sendo responsável pelo design da solução e pelo acompanhamento de um desenvolvedor júnior."
              3. "prazoConclusao": (String) Um prazo realista para a conclusão da ação principal, no formato "AAAA-MM-DD que seja EXCLUSIVAMENTE APÓS 2025-09-10".
              4. "indicadorSucesso": (String) Descreva como o sucesso da ação será medido de forma clara e objetiva. Exemplo: "Entrega da funcionalidade com 95%% de cobertura de testes e feedback positivo do desenvolvedor mentorado."
              5. "recursoResponsavel": (String) Indique quais recursos serão necessários e quem será o responsável por provê-los. Exemplo: "Gestor direto proverá mentoria semanal; Empresa fornecerá acesso à plataforma de cursos online."
              6. "compatibilidade": (String) Uma avaliação concisa do nível de compatibilidade geral do colaborador para a promoção. Use termos como "Muito Alta", "Alta", "Média-Alta", "Média", "Média-Baixa", "Baixa" ou "Incompatível".
              7. "pontosFortes": (Array de Strings) Uma lista detalhando os pontos fortes específicos do colaborador que já o alinham com o próximo nível.
              8. "lacunasIdentificadas": (Array de Strings) Uma lista detalhando as lacunas específicas (técnicas ou comportamentais) que o colaborador precisa desenvolver.
              9. "sugestoesParaEmpresa": (String) Uma sugestão estratégica para o gestor. Exemplo: "O colaborador demonstra alto potencial técnico. Recomenda-se incluí-lo em discussões de design de software para acelerar seu desenvolvimento arquitetural."

              Analise todos os dados e gere o objeto JSON completo, sem nenhum texto ou formatação adicional.
              """,
      colaborador.getNome(),
      nivelAtual.getSenioridade(),
      proximoNivel.getSenioridade(),
      competenciasAtuais,
      proximoNivel.getSenioridade(),
      competenciasRequeridas
    );

    return prompt.strip();
  }
}