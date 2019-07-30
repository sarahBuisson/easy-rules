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
package org.jeasy.rules.api


/**
 * Represents a set of named facts. Facts have unique name within a `Facts` object.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
open class FactsMap : Iterable<Map.Entry<String, Any?>> {


    constructor(facts: Map<String, Any?>?=null)
    {
        if(facts!=null) {
            this.facts.putAll(facts)
        }
    }

    private val facts = mutableMapOf<String, Any?>()
    val size: Int
        get() = facts.size
    val isEmpty: Boolean
        get() = facts.isEmpty()

    /**
     * Put a fact in the working memory.
     * This will replace any fact having the same name.
     *
     * @param name fact name
     * @param fact object to put in the working memory
     * @return the previous value associated with <tt>name</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>name</tt>.
     * (A <tt>null</tt> return can also indicate that the map
     * previously associated <tt>null</tt> with <tt>name</tt>.)
     */
    fun put(name: String, fact: Any?): Any? {
        return facts.put(name, fact)
    }

    /**
     * Remove fact.
     *
     * @param name of fact to remove
     * @return the previous value associated with <tt>name</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>name</tt>.
     * (A <tt>null</tt> return can also indicate that the map
     * previously associated <tt>null</tt> with <tt>name</tt>.)
     */
    fun remove(name: String): Any? {
        return facts.remove(name)
    }

    /**
     * Get a fact by name.
     *
     * @param name of the fact
     * @param <T> type of the fact
     * @return the fact having the given name, or null if there is no fact with the given name
    </T> */
    operator fun <T> get(name: String): T {
        return facts.get(name) as T
    }

    /**
     * Return facts as a map.
     *
     * @return the current facts as a [HashMap]
     */
    fun asMap(): Map<String, Any?> {
        return facts
    }


    override fun iterator(): Iterator<Map.Entry<String, Any?>> {
        return facts.entries.iterator()
    }


    override fun toString(): String {
        var stringBuilder = ("[")
        val entries = ArrayList(facts.entries)
        for (i in 0 until entries.size) {
            val entry = entries.get(i)
            stringBuilder += (" { ${entry.key} : ${entry.value}  } ")
            if (i < entries.size - 1) {
                stringBuilder += (",")
            }
        }
        stringBuilder += ("]")
        return stringBuilder.toString()
    }
}
