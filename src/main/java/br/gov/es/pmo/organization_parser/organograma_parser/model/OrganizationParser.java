package br.gov.es.pmo.organization_parser.organograma_parser.model;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import br.gov.es.pmo.organization_parser.pmo_base.model.IOrganizationParser;
import br.gov.es.pmo.organization_parser.pmo_base.utils.ApiClient;

@Component("org")
public class OrganizationParser implements IOrganizationParser<String> {

    private static final String GUID = "guid";
    private static final String SIGLA = "sigla";
    private static final String RAZAO_SOCIAL = "razaoSocial";

    private static final String GOVES_GUID = "fe88eb2a-a1f3-4cb1-a684-87317baf5a57";

    private final ApiClient apiClient =
        new ApiClient("https://api.organograma.es.gov.br");

    private JSONObject getOrganization(String clientToken) {

        JSONObject resp = apiClient
        .doGetRequest(
            "/organizacoes/" + GOVES_GUID + "/filhas",
            clientToken
        )
        .block();

        return resp;
    }

    @Override
    public String getId(String clientToken) {

        JSONObject organization = getOrganization(clientToken);

        return organization.optString(GUID, null);
    }

    @Override
    public String getNome(String clientToken) {

        JSONObject organization = getOrganization(clientToken);

        return organization.optString(SIGLA, null);
    }

    @Override
    public String getFullName(String clientToken) {

        JSONObject organization = getOrganization(clientToken);

        return organization.optString(RAZAO_SOCIAL, null);
    }
}