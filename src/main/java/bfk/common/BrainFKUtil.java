/*
 * Copyright 2014 Andrew Romanenco
 * www.romanenco.com
 * andrew@romanenco.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bfk.common;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.romanenco.cfrm.Lexer;
import com.romanenco.cfrm.Parser;
import com.romanenco.cfrm.ParsingTreeNode;
import com.romanenco.cfrm.lexer.RegLexer;
import com.romanenco.cfrm.llparser.LLParser;

public final class BrainFKUtil {

    public static final BrainFKGrammar GRAMMAR = new BrainFKGrammar();

    private BrainFKUtil() {
        // nothing
    }

    public static ParsingTreeNode parseSourceFromFile(String fileName)
            throws IOException {
        final String input = BrainFKUtil.getSourceFromFile(fileName);
        return parseSource(input);
    }

    public static ParsingTreeNode parseSource(String input) {
        final Lexer lexer = new RegLexer(GRAMMAR);
        final Parser parser = new LLParser(GRAMMAR);
        return parser.parse(lexer.tokenize(input));
    }

    private static String getSourceFromFile(String fileName) throws IOException {
        final byte[] encoded = Files.readAllBytes(Paths.get(fileName));
        return new String(encoded, Charset.defaultCharset());
    }

}
