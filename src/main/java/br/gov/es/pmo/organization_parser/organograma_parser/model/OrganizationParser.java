package br.gov.es.pmo.organization_parser.organograma_parser.model;

import br.gov.es.pmo.organization_parser.pmo_base.model.IOrganizationParser;
import br.gov.es.pmo.organization_parser.pmo_base.model.OrganizationDto;
import br.gov.es.pmo.organization_parser.pmo_base.utils.ApiClient;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component("org")
public class OrganizationParser
    implements IOrganizationParser<String> {

    private static final String INTEGRATION = "GOVES";
    private static final String SUFFIX = "ES";
    private static final String GUID = "guid";
    private static final String SIGLA = "sigla";
    private static final String RAZAO_SOCIAL = "razaoSocial";

    private static final String GOVES_GUID =
        "fe88eb2a-a1f3-4cb1-a684-87317baf5a57";

    private final ApiClient apiClient =
        new ApiClient("https://api.organograma.es.gov.br");

    public List<OrganizationDto> getOrganizations(String token) {

        String json =
            apiClient
                .doGetRequest(
                    "/organizacoes/"
                        + GOVES_GUID
                        + "/filhas",
                    token
                )
                .block();

        try {
              JSONArray array =
                (JSONArray) new JSONParser(
                    JSONParser.DEFAULT_PERMISSIVE_MODE
                ).parse(json);

            List<OrganizationDto> result = new ArrayList<>();

            for (Object obj : array) {

                JSONObject o = (JSONObject) obj;

                OrganizationDto dto = new OrganizationDto(
                    (String) o.get(GUID),
                    (String) o.get(SIGLA),
                    (String) o.get(RAZAO_SOCIAL),
                    INTEGRATION,
                    SUFFIX
                );

                result.add(dto);
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException(
                "Erro ao parsear organizações",
                e
            );
        }
    }

}