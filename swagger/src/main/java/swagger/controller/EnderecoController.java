package swagger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import swagger.model.EnderecoViaCep;
import swagger.service.ViaCepService;


// Controller responsável pelo endpoint de consulta de endereço
@RestController
public class EnderecoController {

    // Serviço responsavel por realizar a comunicação com a API ViaCEP
    @Autowired
    private ViaCepService viaCepService;

    // Consulta um endereço utilizando o CEP informado na URL
    @GetMapping("/cep/{cep}")
    public EnderecoViaCep buscaEnderecoPorCep(@PathVariable String cep){
        return viaCepService.consultarCep(cep);
    }
}
