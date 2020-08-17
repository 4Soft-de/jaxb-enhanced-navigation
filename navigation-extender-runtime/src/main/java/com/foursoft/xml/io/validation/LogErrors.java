/*-
 * ========================LICENSE_START=================================
 * navigation-extender-runtime
 * %%
 * Copyright (C) 2019 - 2020 4Soft GmbH
 * %%
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
 * =========================LICENSE_END==================================
 */
package com.foursoft.xml.io.validation;

import com.foursoft.xml.io.validation.LogValidator.ErrorLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * helper class to log  {@link ErrorLocation}
 * It will prefix the incorrect lines with "ERROR" and add the error messages
 */
public final class LogErrors {

    static final Pattern LINE_SPLIT_PATTERN = Pattern.compile("\\r?\\n");

    private LogErrors() {

    }

    /**
     * adds all error messages to the xml content
     *
     * @param xmlContent the xml content containing errors
     * @param errorLines the detected errors
     * @return the annotated xml content
     */
    public static String annotateXMLContent(final String xmlContent,
                                            final Collection<ErrorLocation> errorLines) {
        if (errorLines.isEmpty()) {
            return xmlContent;
        }
        final Map<Integer, String> errorMap = prepareErrors(errorLines);

        final String[] lines = LINE_SPLIT_PATTERN.split(xmlContent);
        final StringBuilder output = new StringBuilder(xmlContent.length() * 2);
        for (int i = 1; i <= lines.length; i++) {
            addLineNumber(output, i);
            if (errorMap.containsKey(i)) {
                addErrorLine(output, lines[i - 1], errorMap.get(i));
            } else {
                addNormalLine(output, lines[i - 1]);
            }
        }
        return output.toString();

    }

    static void addNormalLine(final StringBuilder output, final String line) {
        output.append("      ");
        output.append(line);
        addNewLine(output);
    }

    static void addNewLine(final StringBuilder output) {
        output.append('\n');
    }

    static void addLineNumber(final StringBuilder output, final int i) {
        output.append(i);
        output.append(": ");
    }

    static void addErrorLine(final StringBuilder output,
                             final String line,
                             final String error) {
        output.append("ERROR ");
        output.append(line);
        output.append(' ');
        output.append(error);
        addNewLine(output);
    }

    static Map<Integer, String> prepareErrors(final Collection<ErrorLocation> errorLines) {
        final Map<Integer, String> errorMap = new HashMap<>();

        for (final ErrorLocation errorLine : errorLines) {
            final int line = errorLine.line;
            final String error = errorLine.message;
            if (errorMap.containsKey(line)) {
                errorMap.put(line, errorMap.get(line) + "/" + error);
            } else {
                errorMap.put(line, error);
            }
        }
        return errorMap;
    }
}
