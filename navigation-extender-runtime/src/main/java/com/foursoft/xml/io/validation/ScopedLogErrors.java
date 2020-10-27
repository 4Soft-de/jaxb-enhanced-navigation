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
import java.util.Map;

import static com.foursoft.xml.io.validation.LogErrors.*;

/**
 * helper class to log  {@link ErrorLocation}
 * It will prefix the incorrect lines with "ERROR" and add the error messages, also it will truncate all
 * valid lines.  Output for 3 surrounding lines would be e.g.:
 * <pre>{@code
 * ...
 * 12:           <ChildB id="id_6">
 * 13:               <xyz>another value</xyz>
 * 14:           </ChildB>
 * 15: ERROR     <ChildB id="id_6"> cvc-id.2: ID-Wert 'id_6' kommt mehrmals vor./cvc-attribute.3: Wert 'id_6' des
 * Attributs 'id' bei Element 'ChildB' hat keinen gültigen Typ 'ID'.
 * 16:               <xyz>value</xyz>
 * 17:           </ChildB>
 * 18:           <ChildB id="id_71">
 * ...
 * }</pre>
 */
public final class ScopedLogErrors {

    private ScopedLogErrors() {

    }

    /**
     * adds all error messages to the xml content
     *
     * @param xmlContent           the xml content containing errors
     * @param errorLines           the detected errors
     * @param numberOfContextLines number of valid lines printed before+after the incorrect line
     * @return the annotated xml content
     */
    public static String annotateXMLContent(final String xmlContent,
                                            final Collection<ErrorLocation> errorLines,
                                            final int numberOfContextLines) {
        if (errorLines.isEmpty()) {
            return "";
        }
        final Map<Integer, String> errorMap = prepareErrors(errorLines);

        final String[] lines = LINE_SPLIT_PATTERN.split(xmlContent);
        final StringBuilder output = new StringBuilder(xmlContent.length() * 2);
        boolean inSeparatorMode = true;
        for (int i = 1; i <= lines.length; i++) {
            if (!isInScopeOfError(i, errorMap, numberOfContextLines)) {
                if (inSeparatorMode) {
                    addErrorSeparator(output);
                    inSeparatorMode = false;
                }
                continue;
            }
            inSeparatorMode = true;
            addLineNumber(output, i);
            if (errorMap.containsKey(i)) {
                addErrorLine(output, lines[i - 1], errorMap.get(i));
            } else {
                addNormalLine(output, lines[i - 1]);
            }
        }
        return output.toString();

    }

    private static void addErrorSeparator(final StringBuilder output) {
        output.append("...");
        addNewLine(output);
    }

    private static boolean isInScopeOfError(final int pos,
                                            final Map<Integer, String> errorMap,
                                            final int numberOfContextLines) {
        for (int i = -numberOfContextLines; i <= numberOfContextLines; i++) {
            if (errorMap.containsKey(pos + i)) {
                return true;
            }
        }
        return false;
    }
}
