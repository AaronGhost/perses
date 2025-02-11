/*
 * Copyright (C) 2018-2022 University of Waterloo.
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
package org.perses.grammar.c;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.truth.Truth;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.perses.TestUtility;
import org.perses.antlr.ast.AbstractPersesRuleDefAst;
import org.perses.antlr.ast.PersesGrammar;
import org.perses.antlr.ast.RuleNameRegistry;

/** Test for testing the optimized C grammar. */
@RunWith(JUnit4.class)
public class OptCParserTest {

  private static final CParserFacade C_PARSER_FACADE = new CParserFacade();
  private static final PnfCParserFacade PNF_C_PARSER_FACADE = new PnfCParserFacade();

  private static void testOneCFile(Path testFile) {
    try {
      final ArrayList<String> origTokens =
          TestUtility.extractTokenTexts(C_PARSER_FACADE.parseWithOrigCParser(testFile).getTree());
      {
        final ParseTree treeByOpt = C_PARSER_FACADE.parseFile(testFile).getTree();
        assertThat(origTokens)
            .containsExactlyElementsIn(TestUtility.extractTokenTexts(treeByOpt))
            .inOrder();
      }
      {
        final ParseTree treeByPnfc = PNF_C_PARSER_FACADE.parseFile(testFile).getTree();
        assertThat(origTokens)
            .containsExactlyElementsIn(TestUtility.extractTokenTexts(treeByPnfc))
            .inOrder();
      }

    } catch (Throwable e) {
      e.printStackTrace();
      Assert.fail();
    }
  }

  @Test
  public void testAsmStmt_Issue16() throws IOException {
    final Path file = Paths.get("test_data/c_programs/gcc_testsuite/06002.c");
    C_PARSER_FACADE.parseFile(file);
    PNF_C_PARSER_FACADE.parseFile(file);
  }

  /** https://gcc.gnu.org/onlinedocs/gcc/Local-Register-Variables.html#Local-Register-Variables */
  @Test
  public void testRegisterVariableWithAsm() throws IOException {
    final Path file = Paths.get("test_data/c_programs/clang_testsuite/00374.c");
    PNF_C_PARSER_FACADE.parseFile(file);
    C_PARSER_FACADE.parseWithOrigCParser(file);
    C_PARSER_FACADE.parseFile(file);
  }

  @Test
  public void testOptimizedCParserWithOriginalCParserOnClang() throws IOException {
    TestUtility.getGccTestFiles().forEach(OptCParserTest::testOneCFile);
  }

  @Test
  public void testOptimizedCParserWithOriginalCParserOnGcc() throws IOException {
    TestUtility.getGccTestFiles().forEach(OptCParserTest::testOneCFile);
  }

  @Test
  public void testNumOfGccTestFiles() {
    Truth.assertThat(TestUtility.getGccTestFiles().size()).isEqualTo(10850);
    Truth.assertThat(TestUtility.getClangTestFiles().size()).isEqualTo(1799);
  }

  @Test
  public void testIntegrityOfOptimizedCParser() {
    final PersesGrammar persesGrammar = C_PARSER_FACADE.getAntlrGrammar().asCombined().getGrammar();

    final ImmutableList<String> ruleNames =
        persesGrammar.getFlattenedAllRules().stream()
            .filter(AbstractPersesRuleDefAst::isParserRule)
            .map(AbstractPersesRuleDefAst::getRuleNameHandle)
            .map(RuleNameRegistry.RuleNameHandle::getRuleName)
            .collect(ImmutableList.toImmutableList());
    assertThat(ruleNames).containsExactlyElementsIn(TestUtility.OPT_C_PARSER_RULE_NAMES);
  }
}
