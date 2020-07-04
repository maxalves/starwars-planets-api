package integration.com.challenge.starwars;

import com.challenge.starwars.StarWarsApplication;
import com.challenge.starwars.models.dtos.PlanetDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import integration.com.challenge.starwars.controllers.PlanetDTOCollectionModelDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;

@SpringBootTest(classes = StarWarsApplication.class, properties = {"spring.cache.type=NONE"})
public
class StarWarsApplicationTests {

	@Autowired
	private ObjectMapper mapper;

	@Test
	void contextLoads() {
	}

	public String convertToJson(PlanetDTO planetDTO) throws JsonProcessingException {
		return mapper.writeValueAsString(planetDTO);
	}

	public PlanetDTO convertToPlanetDTO(MvcResult result)
			throws JsonProcessingException, UnsupportedEncodingException {
		mapper.configure(MapperFeature.USE_ANNOTATIONS, false);

		return mapper.readValue(result.getResponse().getContentAsString(), PlanetDTO.class);
	}

	public CollectionModel<PlanetDTO> convertToCollection(MvcResult result)
			throws JsonProcessingException, UnsupportedEncodingException {
		mapper.configure(MapperFeature.USE_ANNOTATIONS, false);
		var planetDTOCollectionModelType = new TypeReference<CollectionModel<PlanetDTO>>() {};
		SimpleModule module = new SimpleModule();
		module.addDeserializer(
				convertToClass(planetDTOCollectionModelType),
				new PlanetDTOCollectionModelDeserializer());

		mapper.registerModule(module);

		return mapper.readValue(result.getResponse().getContentAsString(),
				planetDTOCollectionModelType);
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> convertToClass(TypeReference<T> ref) {
		return (Class<T>)((ParameterizedType) ref.getType()).getRawType();
	}
}
