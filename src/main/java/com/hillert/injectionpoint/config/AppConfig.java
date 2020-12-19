/*
 * Copyright 2020 Gunnar Hillert
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hillert.injectionpoint.config;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.hillert.injectionpoint.services.GreeterService;
import com.hillert.injectionpoint.services.Greeting;
import com.hillert.injectionpoint.services.Language;

/**
 *
 * @author Gunnar Hillert
 *
 */
@Configuration
public class AppConfig {

	@Autowired
	private GreeterService greeterService;

	// This Works

	@Bean
	@Greeting(language = Language.DE)
	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
	public String greetingDE(InjectionPoint injectionPoint) {
		return handleInjectionPoint(injectionPoint);
	}

	@Bean
	@Greeting  //<-- Only valid for EN (the default value)
	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
	public String greetingEN(InjectionPoint injectionPoint) {
		return handleInjectionPoint(injectionPoint);
	}

	@Bean
	@Greeting(language = Language.ES)
	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
	public String greetingES(InjectionPoint injectionPoint) {
		return handleInjectionPoint(injectionPoint);
	}

	/**
	 * This SHOULD Work BUT Does Not
	 * Spring Should ignore properties marked with the {@code Nonbinding} annotation
	 * on javax.inject.Qualifier annotations
	 *
	 * IF you remove the javax.inject.Qualifier annotation from {@link Greeting} then this method
	 * will work also. Qualifiers are "special" and are handled specifically in
	 * {@link QualifierAnnotationAutowireCandidateResolver}.
	 */
//	@Bean
//	@Greeting
//	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
//	public String greetingES(InjectionPoint injectionPoint) {
//		return handleInjectionPoint(injectionPoint);
//	}

	private String handleInjectionPoint(InjectionPoint injectionPoint) {
		final Greeting greeting = findAnnotation(injectionPoint.getAnnotatedElement(), Greeting.class);

		final String localizedGreeting;

		switch (greeting.language()) {
			case DE:
				localizedGreeting = "Hallo aus Berlin!";
				break;
			case ES:
				localizedGreeting = "¡Hola desde Medellín!";
				break;
			case EN:
			default:
				localizedGreeting = "Hello from the Big Island of Hawaii!";
		}
		return localizedGreeting;
	}

	@Bean
	@Profile("!test")
	public CommandLineRunner printToConsole() {
		return  args -> {
			final String caller =
				!ObjectUtils.isEmpty(args) && StringUtils.hasText(args[0]) ? args[0] : "Incognito";
			System.out.println(StringUtils.collectionToDelimitedString(this.greeterService.sayHello(caller), "\n"));
		};
	}

}
