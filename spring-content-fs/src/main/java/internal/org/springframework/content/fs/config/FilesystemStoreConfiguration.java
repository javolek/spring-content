package internal.org.springframework.content.fs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.content.commons.utils.PlacementService;
import org.springframework.content.commons.utils.PlacementServiceImpl;
import org.springframework.content.fs.config.FilesystemStoreConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;

import java.net.URI;
import java.util.List;

@Configuration
public class FilesystemStoreConfiguration {

	@Autowired(required = false)
	private List<FilesystemStoreConfigurer> configurers;

	@Bean
	public PlacementService filesystemStorePlacementService() {
		PlacementService conversion = new PlacementServiceImpl();
		conversion.addConverter(new Converter<URI, String>() {
			@Override
			public String convert(URI source) {
				return source.toString();
			}
		});

		addConverters(conversion);
		return conversion;
	}

	protected void addConverters(ConverterRegistry registry) {
		if (configurers == null)
			return;

		for (FilesystemStoreConfigurer configurer : configurers) {
			configurer.configureFilesystemStoreConverters(registry);
		}
	}
}
