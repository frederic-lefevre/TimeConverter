/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package org.fl.timeConverter;

import static org.assertj.core.api.Assertions.*;

import java.net.URISyntaxException;

import org.fl.util.RunningContext;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

class ConfigTest {

	private static final String APPLICATION_NAME = "org.fl.timeConverter";
	
	@Test
	void runningContextTest() throws URISyntaxException {
		
		RunningContext runningContext = Config.getRunningContext();
		
		assertThat(runningContext).isNotNull();
		assertThat(runningContext.getName()).isNotNull().isEqualTo(APPLICATION_NAME);
		
		JsonNode applicationInfo = runningContext.getApplicationInfo(false);
		assertThat(applicationInfo).isNotNull();
		
		JsonNode buildInformation = applicationInfo.get("buildInformation");
		assertThat(buildInformation).isNotEmpty().hasSize(2)
		.satisfiesExactlyInAnyOrder(
				buildInfo -> { 
					assertThat(buildInfo.get("moduleName")).isNotNull();
					assertThat(buildInfo.get("moduleName").asText()).isEqualTo(APPLICATION_NAME);
				},
				buildInfo -> { 
					assertThat(buildInfo.get("moduleName")).isNotNull();
					assertThat(buildInfo.get("moduleName").asText()).isEqualTo("org.fl.util");
				}
				);
	}
	
	@Test
	void buildInformationTest() throws JsonProcessingException, URISyntaxException {
		
		RunningContext runningContext = Config.getRunningContext();
		
		assertThat(runningContext).isNotNull();
		
		JsonNode buildInformation = runningContext.getBuildInformationAsJson();
		assertThat(buildInformation).isNotNull();

		assertThat(buildInformation).isNotEmpty().hasSize(2)
			.satisfiesExactlyInAnyOrder(
				buildInfo -> assertModuleBuildInfo(buildInfo, APPLICATION_NAME),
				buildInfo -> assertModuleBuildInfo(buildInfo, "org.fl.util")
			);
	}
	
	private void assertModuleBuildInfo(JsonNode buildInfo, String moduleName) {
		assertThat(buildInfo).hasSize(11);
		assertThat(buildInfo.get("moduleName")).isNotNull();
		assertThat(buildInfo.get("moduleName").asText()).isEqualTo(moduleName);
		assertThat(buildInfo.has("version")).isTrue();
		assertThat(buildInfo.has("buildtime")).isTrue();
		assertThat(buildInfo.has("builder")).isTrue();
		assertThat(buildInfo.has("buildhost")).isTrue();
		assertThat(buildInfo.has("buildOs")).isTrue();
		assertThat(buildInfo.has("gitBranch")).isTrue();
		assertThat(buildInfo.has("gitCommitId")).isTrue();
		assertThat(buildInfo.has("gitCommitUrl")).isTrue();
		assertThat(buildInfo.has("gitCommitTime")).isTrue();
		assertThat(buildInfo.has("gitDirty")).isTrue();
	}
}
