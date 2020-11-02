package com.foursoft.xml.io.write.processinginstructions;/*-
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.Marshaller.Listener;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Optional;

public class ProcessingInstructionAdderListener extends Listener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingInstructionAdderListener.class);
    private final XMLStreamWriter xsw;
    private final ProcessingInstructions processingInstructions;

    /**
     * @param xsw the xml stream writer
     * @param processingInstructions map of xjc objects and comment strings
     */
    public ProcessingInstructionAdderListener(final XMLStreamWriter xsw, final ProcessingInstructions processingInstructions) {
        this.xsw = xsw;
        this.processingInstructions = processingInstructions;
    }

    @Override
    public void beforeMarshal(final Object source) {
        final Optional<String> processingInstruction = processingInstructions.get(source);
        if (processingInstruction.isPresent()) {
            try {
                xsw.writeProcessingInstruction(processingInstruction.get());
            } catch (final XMLStreamException e) {
                LOGGER.warn("Ignored Exception while writing processingInstruction:", e);
            }
        }
    }
}
