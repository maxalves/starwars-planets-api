package integration.com.challenge.starwars.controllers;

import com.challenge.starwars.models.dtos.PlanetDTO;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.TextNode;
import integration.com.challenge.starwars.StarWarsApplicationTests;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlanetDTOCollectionModelDeserializer extends JsonDeserializer<CollectionModel<PlanetDTO>> {

    @Override
    public CollectionModel<PlanetDTO> deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        var mapper = new ObjectMapper();
        mapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        var resultNode = parser.readValueAsTree();

        var planetDTOCollectionModel = CollectionModel.of(getPlanetDTOList(mapper, resultNode));

        planetDTOCollectionModel.add(getLinks(resultNode));

        return planetDTOCollectionModel;
    }

    private List<PlanetDTO> getPlanetDTOList(ObjectMapper mapper, TreeNode resultNode) throws IOException {
        if (Objects.isNull(resultNode.get("_embedded")))
            return new ArrayList<>();

        var planetNodeList = resultNode.get("_embedded").get("items");

        var planetDTOListType = new TypeReference<List<PlanetDTO>>() {};
        return mapper.readValue(
                planetNodeList.traverse(),
                StarWarsApplicationTests.convertToClass(planetDTOListType)
        );
    }

    private ArrayList<Link> getLinks(TreeNode resultNode) {
        var linksResult = resultNode.get("_links");
        var links = new ArrayList<Link>();

        if (Objects.nonNull(linksResult.get("next")))
            links.add(convertToLink("next", linksResult.get("next")));

        if(Objects.nonNull(linksResult.get("previous")))
            links.add(convertToLink("previous", linksResult.get("previous")));

        return links;
    }

    private Link convertToLink(String name, TreeNode linkNode) {
        return Link.of(((TextNode) linkNode.get("href")).asText(), name);
    }

}
