package swagger.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import swagger.model.EnderecoViaCep;

// Serviço responsável pela comunicação com a API externa ViaCEP
@Service
public class ViaCepService {

    // Cliente HTTP utilizado para realizar a requisição à API externa
    private final RestTemplate restTemplate = new RestTemplate();
    // URL base utilizada para consultar os CEPs
    private final String urlBase = "https://viacep.com.br/ws/";

    // Consulta um CEP e converte a resposta para o objeto EnderecoViaCep
    public EnderecoViaCep consultarCep(String cep) {
        String url = urlBase + cep + "/json/";
        return restTemplate.getForObject(url, EnderecoViaCep.class);
    }
}
