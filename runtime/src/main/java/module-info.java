/*-
 * ========================LICENSE_START=================================
 * navigation-extender-runtime
 * %%
 * Copyright (C) 2019 - 2022 4Soft GmbH
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
open module com.foursoft.jaxb.navext.runtime {
    requires org.slf4j;
    requires java.xml.bind;
    requires org.glassfish.jaxb.runtime;

    exports com.foursoft.jaxb.navext.runtime;
    exports com.foursoft.jaxb.navext.runtime.annotations;
    exports com.foursoft.jaxb.navext.runtime.cache;
    exports com.foursoft.jaxb.navext.runtime.io.read;
    exports com.foursoft.jaxb.navext.runtime.io.utils;
    exports com.foursoft.jaxb.navext.runtime.io.write;
    exports com.foursoft.jaxb.navext.runtime.io.write.xmlmeta;
    exports com.foursoft.jaxb.navext.runtime.io.write.xmlmeta.comments;
    exports com.foursoft.jaxb.navext.runtime.io.write.xmlmeta.processinginstructions;
    exports com.foursoft.jaxb.navext.runtime.io.validation;
    exports com.foursoft.jaxb.navext.runtime.model;
    exports com.foursoft.jaxb.navext.runtime.postprocessing;
    exports com.foursoft.jaxb.navext.runtime.io.write.id;
}
