/*
 * Copyright (C) 2018-2020 University of Waterloo.
 *
 * This file is part of Perses.
 *
 * Perses is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3, or (at your option) any later version.
 *
 * Perses is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Perses; see the file LICENSE.  If not see <http://www.gnu.org/licenses/>.
 */
package org.perses.grammar.glsl;

import com.google.common.primitives.ImmutableIntArray;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.perses.antlr.ParseTreeWithParser;
import org.perses.grammar.AbstractDefaultParserFacade;

public final class PnfGlslParserFacade
    extends AbstractDefaultParserFacade<PnfGlslLexer, PnfGlslParser> {

  public PnfGlslParserFacade() {
    super(
            LanguageGlsl.INSTANCE,
            createCombinedAntlrGrammar("PnfGlsl.g4", PnfGlslParserFacade.class),
            PnfGlslLexer.class,
            PnfGlslParser.class,
            ImmutableIntArray.of(PnfGlslLexer.IDENTIFIER));
  }

  @Override
  protected PnfGlslLexer createLexer(CharStream inputStream) {
    return new PnfGlslLexer(inputStream);
  }

  @Override
  protected PnfGlslParser createParser(CommonTokenStream tokens) {
    return new PnfGlslParser(tokens);
  }

  @Override
  protected ParseTree startParsing(PnfGlslParser parser) {
    return parser.translation_unit();
  }

  public ParseTreeWithParser parseWithOrigGlslParser(Path file) throws IOException {
    try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
      return parseWithOrigGlslParser(reader, file.toString());
    }
  }

  public ParseTreeWithParser parseWithOrigGlslParser(String glslProgram) throws IOException {
    try (BufferedReader reader = new BufferedReader(new StringReader(glslProgram))) {
      return parseWithOrigGlslParser(reader, "<in-memory>");
    }
  }

  public ParseTreeWithParser parseWithOrigGlslParser(String glslProgram, String fileName)
          throws IOException {
    try (BufferedReader reader = new BufferedReader(new StringReader(glslProgram))) {
      return parseWithOrigGlslParser(reader, fileName);
    }
  }

  private static ParseTreeWithParser parseWithOrigGlslParser(
          BufferedReader reader, String fileName) throws IOException {
    return parseReader(
            fileName,
            reader,
            charStream -> new GLSLLexer(charStream),
            commonTokenStream -> new GLSLParser(commonTokenStream),
            GLSLParser::translation_unit);
  }
}

