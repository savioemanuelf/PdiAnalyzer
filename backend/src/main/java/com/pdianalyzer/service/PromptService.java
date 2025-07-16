package com.pdianalyzer.service;

import com.pdianalyzer.domain.model.Colaborador;
import com.pdianalyzer.domain.model.MetricasDesempenho;
import com.pdianalyzer.domain.model.Nivel;
import com.smarthirepro.domain.model.Candidato;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromptService {

  public List<String> gerarPromptDeAnaliseDeGaps(Colaborador colaborador, Nivel proximoNivel, MetricasDesempenho metricas, List<String> criterios) {

    String nomeColaborador = colaborador.getNome();
    String senioridadeAtual = colaborador.getCargo().getNivel().getSenioridade();
    String senioridadeDesejada = proximoNivel.getSenioridade();
    String criteriosTexto = String.join("\n- ", criterios);

    String prompt = """
      Aja como um especialista em desenvolvimento humano estratégico e inteligência de carreira. Sua tarefa é criar um **Plano de Desenvolvimento Individual (PDI)** robusto, com insights **práticos, previsões de retorno financeiro**, e **ações transformadoras** com base na situação atual de um colaborador e nos critérios exigidos para o próximo nível.

      ## 🔍 Contexto do Colaborador:
      - 👤 Nome: %s
      - 🧭 Nível Atual: %s
      - 🎯 Próximo Nível Alvo: %s

      ## 📊 Situação Atual (Métricas de Desempenho):
      - Competências Técnicas: %s
      - Competências Pessoais: %s
      - Pontos de Melhoria: %s

      ## 📌 Critérios para o próximo nível (%s):
      - %s

      ## 🧠 Sua Tarefa:

      Gere uma resposta em formato **JSON estruturado** com os seguintes campos:

      1. `compatibilidade_percentual`: De 0.0 a 1.0 — o quanto este colaborador já está preparado para o próximo nível. Cite o nome do colaborador.
      2. `pontos_fortes`: Lista de pontos já dominados por ele.
      3. `gaps_de_desenvolvimento`: Lista de gaps técnicos, comportamentais ou de experiência ainda a serem trabalhados.
      4. `plano_de_acao_sugerido`: Lista com ao menos **4 ações práticas**, como:
         - Cursos online (indique links reais, como Udemy, Alura, Coursera),
         - Projetos internos desafiadores,
         - Sessões de mentoria com colegas mais experientes,
         - Leitura de livros ou estudos de caso.
      5. `tempo_estimado_de_progressao`: Caso se veja que não é o momento de promover o colaborador, estimativa realista (em meses) de quanto tempo levará para atingir o novo nível, com base nas `recomendacoes_para_gestor`.
      6. `estimativa_salarial_nova_posicao`: Valor médio do salário da nova posição no Brasil (pode inventar valores realistas com base na senioridade e em fontes da sua base de conhecimento).
      7. `retorno_financeiro_potencial`: Diferença percentual e absoluta entre o salário atua de R$3400 reais e o da nova posição.
      8. `recomendacoes_para_gestor`: Parágrafo estratégico com recomendações sobre como o gestor pode acelerar essa transição — ex: "reconhecer conquistas visíveis", "permitir mais autonomia", "acompanhar mensalmente via 1:1s". 
      O objetivo aqui é dizer se vale a pena ou não promover o colaborador.

      Seja preciso, inspirador e estratégico. Imagine que sua resposta será apresentada em um comitê de liderança buscando justificar o investimento no crescimento deste colaborador.
      """.formatted(
      nomeColaborador,
      senioridadeAtual,
      senioridadeDesejada,
      metricas.getCompetenciasTecnicas(),
      metricas.getCompetenciasPessoais(),
      metricas.getPontosMelhoria(),
      senioridadeDesejada,
      criteriosTexto
    );

    return List.of(prompt);
  }


}
