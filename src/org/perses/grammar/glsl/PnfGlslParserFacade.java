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

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.perses.grammar.AbstractDefaultParserFacade;

public final class PnfGlslParserFacade
    extends AbstractDefaultParserFacade<PnfGlslLexer, PnfGlslParser> {

  public PnfGlslParserFacade() {
    super(
        LanguageGlsl.INSTANCE,
        createCombinedAntlrGrammar("PnfGlsl.g4", PnfGlslParserFacade.class));
  }

  @Override
  protected PnfGlslLexer createLexer(ANTLRInputStream inputStream) {
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

  /*
  public ParseTreeWithParser parseWithOrigGlslParser(File file) throws IOException {
    try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
      return parseWithOrigGlslParser(reader, file.getPath());
    }
  }

  public ParseTreeWithParser parseWithOrigGlslParser(String GlslProgram) throws IOException {
    try (BufferedReader reader = new BufferedReader(new StringReader(GlslProgram))) {
      return parseWithOrigGlslParser(reader, "<in-memory>");
    }
  }

  public ParseTreeWithParser parseWithOrigGlslParser(String goProgram, String fileName)
      throws IOException {
    try (BufferedReader reader = new BufferedReader(new StringReader(goProgram))) {
      return parseWithOrigGlslParser(reader, fileName);
    }
  }

  private static ParseTreeWithParser parseWithOrigGlslParser(
      BufferedReader reader, String fileName) throws IOException {
    return parseReader(
        fileName,
        reader,
        antlrInputStream -> new GlslLexer(antlrInputStream),
        commonTokenStream -> new GlslParser(commonTokenStream),
            GlslParser::translation_unit);
  }
   */
}
