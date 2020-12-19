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
package com.hillert.injectionpoint.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Gunnar Hillert
 *
 */
@Service
public class GreeterService {

	@Autowired
	@Greeting(language = Language.DE)
	private String germanGreeting;

	@Autowired
	@Greeting
	private String englishGreeting;

	@Autowired
	@Greeting(language = Language.ES)
	private String spanishGreeting;

	public List<String> sayHello(String caller) {
		final List<String> helloList = new ArrayList<>(3);
		helloList.add(String.format("%s - %s", caller, germanGreeting));
		helloList.add(String.format("%s - %s", caller, englishGreeting));
		helloList.add(String.format("%s - %s", caller, spanishGreeting));
		return helloList;
	}

}