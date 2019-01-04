/**
 * The MIT License
 *
 * Copyright (c) 2018, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jeasy.rules.mvel

import org.jeasy.rules.api.Rule
import org.junit.Test

import java.io.File
import java.io.FileReader
import java.io.StringReader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Collections

import org.assertj.core.api.Assertions.assertThat

class MVELRuleDefinitionReaderTest {

    private val ruleDefinitionReader = MVELRuleDefinitionReader()

    @Test
    @Throws(Exception::class)
    fun testRuleDefinitionReadingFromFile() {
        // given
        val adultRuleDescriptor = File("src/test/resources/adult-rule.yml")

        // when
        val adultRuleDefinition = ruleDefinitionReader.read(FileReader(adultRuleDescriptor))

        // then
        assertThat(adultRuleDefinition).isNotNull()
        assertThat(adultRuleDefinition.name).isEqualTo("adult rule")
        assertThat(adultRuleDefinition.description).isEqualTo("when age is greater then 18, then mark as adult")
        assertThat(adultRuleDefinition.priority).isEqualTo(1)
        assertThat(adultRuleDefinition.condition).isEqualTo("person.age > 18")
        assertThat(adultRuleDefinition.actions).isEqualTo(Collections.singletonList("person.setAdult(true);"))
    }

    @Test
    @Throws(Exception::class)
    fun testRuleDefinitionReadingFromString() {
        // given
        val adultRuleDescriptor = String(Files.readAllBytes(Paths.get("src/test/resources/adult-rule.yml")))

        // when
        val adultRuleDefinition = ruleDefinitionReader.read(StringReader(adultRuleDescriptor))

        // then
        assertThat(adultRuleDefinition).isNotNull()
        assertThat(adultRuleDefinition.name).isEqualTo("adult rule")
        assertThat(adultRuleDefinition.description).isEqualTo("when age is greater then 18, then mark as adult")
        assertThat(adultRuleDefinition.priority).isEqualTo(1)
        assertThat(adultRuleDefinition.condition).isEqualTo("person.age > 18")
        assertThat(adultRuleDefinition.actions).isEqualTo(Collections.singletonList("person.setAdult(true);"))
    }

    @Test
    @Throws(Exception::class)
    fun testRuleDefinitionReading_withDefaultValues() {
        // given
        val adultRuleDescriptor = File("src/test/resources/adult-rule-with-default-values.yml")

        // when
        val adultRuleDefinition = ruleDefinitionReader.read(FileReader(adultRuleDescriptor))

        // then
        assertThat(adultRuleDefinition).isNotNull()
        assertThat(adultRuleDefinition.name).isEqualTo(Rule.DEFAULT_NAME)
        assertThat(adultRuleDefinition.description).isEqualTo(Rule.DEFAULT_DESCRIPTION)
        assertThat(adultRuleDefinition.priority).isEqualTo(Rule.DEFAULT_PRIORITY)
        assertThat(adultRuleDefinition.condition).isEqualTo("person.age > 18")
        assertThat(adultRuleDefinition.actions).isEqualTo(Collections.singletonList("person.setAdult(true);"))
    }

    @Test(expected = IllegalArgumentException::class)
    @Throws(Exception::class)
    fun testInvalidRuleDefinitionReading_whenNoCondition() {
        // given
        val adultRuleDescriptor = File("src/test/resources/adult-rule-without-condition.yml")

        // when
        val adultRuleDefinition = ruleDefinitionReader.read(FileReader(adultRuleDescriptor))

        // then
        // expected exception
    }

    @Test(expected = TypeCastException::class)
    @Throws(Exception::class)
    fun testInvalidRuleDefinitionReading_whenNoActions() {
        // given
        val adultRuleDescriptor = File("src/test/resources/adult-rule-without-actions.yml")

        // when
        val adultRuleDefinition = ruleDefinitionReader.read(FileReader(adultRuleDescriptor))

        // then
        // expected exception
    }

    @Test
    @Throws(Exception::class)
    fun testRulesDefinitionReading() {
        // given
        val rulesDescriptor = File("src/test/resources/rules.yml")

        // when
        val ruleDefinitions = ruleDefinitionReader.readAll(FileReader(rulesDescriptor))

        // then
        assertThat(ruleDefinitions).hasSize(2)
        var ruleDefinition = ruleDefinitions.get(0)
        assertThat(ruleDefinition).isNotNull()
        assertThat(ruleDefinition.name).isEqualTo("adult rule")
        assertThat(ruleDefinition.description).isEqualTo("when age is greater then 18, then mark as adult")
        assertThat(ruleDefinition.priority).isEqualTo(1)
        assertThat(ruleDefinition.condition).isEqualTo("person.age > 18")
        assertThat(ruleDefinition.actions).isEqualTo(Collections.singletonList("person.setAdult(true);"))

        ruleDefinition = ruleDefinitions.get(1)
        assertThat(ruleDefinition).isNotNull()
        assertThat(ruleDefinition.name).isEqualTo("weather rule")
        assertThat(ruleDefinition.description).isEqualTo("when it rains, then take an umbrella")
        assertThat(ruleDefinition.priority).isEqualTo(2)
        assertThat(ruleDefinition.condition).isEqualTo("rain == true")
        assertThat(ruleDefinition.actions).isEqualTo(Collections.singletonList("System.out.println(\"It rains, take an umbrella!\");"))
    }

    @Test
    @Throws(Exception::class)
    fun testEmptyRulesDefinitionReading() {
        // given
        val rulesDescriptor = File("src/test/resources/rules-empty.yml")

        // when
        val ruleDefinitions = ruleDefinitionReader.readAll(FileReader(rulesDescriptor))

        // then
        assertThat(ruleDefinitions).hasSize(0)
    }
}