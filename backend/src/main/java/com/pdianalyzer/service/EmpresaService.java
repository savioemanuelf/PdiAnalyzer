package com.pdianalyzer.service;

import com.pdianalyzer.domain.DTO.EmpresaPatchRequestDto;
import com.pdianalyzer.domain.DTO.EmpresaResponseDTO;
import com.pdianalyzer.domain.repository.EmpresaRepositoryJpa;
import com.pdianalyzer.domain.repository.CargoRepositoryJpa;
import com.smarthirepro.core.exception.BusinessRuleException;
import com.smarthirepro.core.security.AuthUtils;
import com.smarthirepro.domain.model.Empresa;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmpresaService {

    @Autowired
    private CargoRepositoryJpa cargoRepository;

    @Autowired
    private EmpresaRepositoryJpa empresaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Empresa> findById(UUID id) {

        return ((JpaRepository<Empresa, UUID>) empresaRepository).findById(id);
    }

    @Transactional
    public Empresa salvar(Empresa empresa) {
        boolean cnpjEmUso = empresaRepository.findByCnpj(empresa.getCnpj())
                .filter(e -> !e.equals(empresa))
                .isPresent();
        if (cnpjEmUso) {
            throw new BusinessRuleException("CNPJ já cadastrado no sistema.");
        }

        String senhaCriptografada = passwordEncoder.encode(empresa.getSenha());
        empresa.setSenha(senhaCriptografada);

        return ((JpaRepository<Empresa, UUID>) empresaRepository).save(empresa);
    }
    public List<EmpresaResponseDTO> listarTodas() {
        List<Empresa> empresas = empresaRepository.findAll();
        if (empresas.isEmpty()) {
            throw new BusinessRuleException("Nenhuma empresa encontrada.");
        }
        return empresas.stream()
                .map(EmpresaResponseDTO::new)
                .toList();
    }

    public EmpresaResponseDTO buscarEmpresa() {
        UUID empresaId = AuthUtils.getEmpresaId();
        Empresa empresa = this.findById(empresaId)
                .orElseThrow(() -> new UsernameNotFoundException("Empresa não encontrada."));
        return new EmpresaResponseDTO(empresa);
    }

    public Empresa atualizarEmpresaPorId(EmpresaPatchRequestDto data) {
        UUID id = AuthUtils.getEmpresaId();
        Empresa empresa = this.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        if (data.nome() != null && !data.nome().isBlank())
            empresa.setNome(data.nome());
        if (data.cnpj() != null && !data.cnpj().isBlank())
            empresa.setCnpj(data.cnpj());
        if (data.email() != null && !data.email().isBlank())
            empresa.setEmail(data.email());
        if (data.senha() != null && !data.senha().isBlank())
            empresa.setSenha(new BCryptPasswordEncoder().encode(data.senha()));

        return ((JpaRepository<Empresa, UUID>) empresaRepository).save(empresa);
    }

    @Transactional
    public void excluir() {
        UUID id = AuthUtils.getEmpresaId();
        Empresa empresa = this.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Empresa não encontrada."));

        cargoRepository.deleteAllByEmpresaId(id);
        empresaRepository.delete(empresa);
    }
}