package org.jeasy.rules.tutorials.weather

import org.jeasy.rules.annotation.Action
import org.jeasy.rules.annotation.Condition
import org.jeasy.rules.annotation.Fact
import org.jeasy.rules.annotation.Rule

@Rule(name = "weather rule", description = "if it rains then take an umbrella")
class WeatherRule {

    @Condition
    fun itRains(@Fact("rain") rain: Boolean): Boolean {
        return rain
    }

    @Action
    fun takeAnUmbrella() {
        System.out.println("It rains, take an umbrella!")
    }
}