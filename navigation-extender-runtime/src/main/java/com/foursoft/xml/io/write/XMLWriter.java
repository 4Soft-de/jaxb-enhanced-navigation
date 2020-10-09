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
package com.foursoft.xml.io.write;

import com.foursoft.xml.io.utils.ValidationEventLogger;
import com.foursoft.xml.io.utils.XMLIOException;

import javax.xml.bind.*;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * Serializes a valid JAXB object structure to XML with the following additional features:
 * - Can add comments to the xml file
 * - Formats the XML output
 */
public class XMLWriter<T> {

    private static final String NAMESPACE_PREFIX_MAPPER = "com.sun.xml.bind.namespacePrefixMapper";

    private final Class<T> baseType;
    private final Marshaller marshaller;

    public XMLWriter(final Class<T> baseType) {
        this(baseType, new ValidationEventLogger());
    }

    public XMLWriter(final Class<T> baseType,
                     final Consumer<ValidationEvent> validationEventConsumer) {
        this.baseType = baseType;
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(this.baseType);
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(NAMESPACE_PREFIX_MAPPER, new NamespacePrefixMapperImpl());
            addEventHandler(marshaller, validationEventConsumer);
        } catch (final Exception e) {
            throw new XMLIOException("Cannot initialize unmarshaller.", e);
        }
    }

    /**
     * write the JAXB model to an output stream
     *
     * @param container    the jaxb model to deserialize into the given stream
     * @param outputStream the output to write to
     */
    public void write(final T container, final OutputStream outputStream) {
        write(container, new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
    }

    /**
     * write the JAXB model to an output stream
     *
     * @param container    the jaxb model to deserialize into the given stream
     * @param outputStream the output to write to
     * @param comments     additional comments which should be added to output {@link Comments}
     */
    public void write(final T container, final Comments comments, final OutputStream outputStream) {
        write(container, comments, new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
    }

    /**
     * write the JAXB model to a string
     *
     * @param container the jaxb model to deserialize into the given stream
     * @param comments  additional comments which should be added to output {@link Comments}
     * @return the model as xml string
     */
    public String writeToString(final T container, final Comments comments) {
        try (final StringWriter stringWriter = new StringWriter()) {
            write(container, comments, stringWriter);
            return stringWriter.toString();
        } catch (final Exception e) {
            throw new XMLIOException("Error serializing objects to XML.", e);
        }
    }

    /**
     * write the JAXB model to a string
     *
     * @param container the jaxb model to deserialize into the given stream
     * @return the model as xml string
     */
    public String writeToString(final T container) {
        try (final StringWriter stringWriter = new StringWriter()) {
            write(container, stringWriter);
            return stringWriter.toString();
        } catch (final IOException e) {
            throw new XMLIOException("Error serializing objects to XML", e);
        }
    }

    private static void addEventHandler(final Marshaller marshaller,
                                        final Consumer<ValidationEvent> validationEventConsumer)
            throws JAXBException {
        final ValidationEventHandler eventHandler = marshaller.getEventHandler();
        marshaller.setEventHandler(event -> {
            validationEventConsumer.accept(event);
            return eventHandler.handleEvent(event);
        });

    }

    private void write(final T container, final Comments comments, final Writer output) {
        final XMLOutputFactory xof = XMLOutputFactory.newFactory();
        try {
            final CommentAwareXMLStreamWriter xsw = new CommentAwareXMLStreamWriter(xof.createXMLStreamWriter(output));
            marshaller.setListener(new CommentAdderListener(xsw, comments));
            marshaller.marshal(container, xsw);
            xsw.close();
        } catch (final XMLStreamException | JAXBException e) {
            throw new XMLIOException("Error serializing XML file.", e);
        }
    }

    private void write(final T container, final Writer output) {
        try {
            marshaller.marshal(container, output);
        } catch (final JAXBException e) {
            throw new XMLIOException("Error serializing XML file.", e);
        }
    }

}
