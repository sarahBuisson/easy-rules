/**
 * The MIT License
 *
 * Copyright (c) 2017, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.jeasy.rules.tutorials.web


import org.jeasy.rules.api.FactsMap
import java.io.IOException
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import org.jeasy.rules.api.RulesEngine
import org.jeasy.rules.core.DefaultRulesEngine
import org.jeasy.rules.core.Rules2

@WebFilter("/*")
class SuspiciousRequestFilter : Filter {

    private lateinit var rules: Rules2
    private lateinit var  rulesEngine: RulesEngine<FactsMap>


    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {
        rulesEngine = DefaultRulesEngine()
        rules = Rules2()
        rules.register(SuspiciousRequestRule())
    }


    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain) {
        val facts = FactsMap()
        facts.put("request", request)
        rulesEngine.fire(rules, facts)
        filterChain.doFilter(request, response)
    }


    override fun destroy() {

    }
}
