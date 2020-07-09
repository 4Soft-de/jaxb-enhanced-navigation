package com.foursoft.xml.io.write;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.Marshaller.Listener;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class CommentAdderListener extends Listener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentAdderListener.class);
    private final XMLStreamWriter xsw;
    private final Comments comments;

    /**
     * @param comments map of xjc objects and comment strings
     */
    public CommentAdderListener(final XMLStreamWriter xsw, final Comments comments) {
        this.xsw = xsw;
        this.comments = comments;
    }

    @Override
    public void beforeMarshal(final Object source) {
        if (comments.containsKey(source)) {
            try {
                xsw.writeComment(comments.get(source));
            } catch (final XMLStreamException e) {
                LOGGER.warn("Ignored Exception while writing comments:", e);
            }
        }
    }
}