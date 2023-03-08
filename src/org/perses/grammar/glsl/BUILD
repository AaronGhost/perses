package(default_visibility = [
    "//visibility:public",
])

load("//antlropt/org/perses/antlr/pnf:pnf.bzl", "pnf_convert")
load(
    "//src/org/perses/grammar:antlr_util.bzl",
    "antlr_codegen_combined",
)
load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

#antlr_codegen_combined(
#    name = "orig_glsl_grammar",
#    grammar_file = "GLSL.g4",
#    java_pkg_name = "org.perses.grammar.glsl",
#    lexer_java_file_name = "GlslLexer.java",
#    parser_java_file_name = "GlslParser.java",
#)

pnf_convert(
    name = "normalize_glsl_to_pnf",
    src = "GLSL.g4",
    out = "PnfGlsl.g4",
    start_rule_name = "translation_unit",
)

antlr_codegen_combined(
    name = "pnf_glsl_grammar",
    grammar_file = "PnfGlsl.g4",
    java_pkg_name = "org.perses.grammar.glsl",
    lexer_java_file_name = "PnfGlslLexer.java",
    parser_java_file_name = "PnfGlslParser.java",
)

java_library(
    name = "pnfglsl_parser_facade",
    srcs = ["PnfGlslParserFacade.java"],
    deps = [
        ":language_kind",
        #":orig_glsl_grammar",
        ":pnf_glsl_grammar",
        "//src/org/perses/antlr:parse_tree_with_parser",
        "//src/org/perses/grammar:grammar_facade",
        "//src/org/perses/program:language_kind",
        "@maven//:com_google_flogger_flogger",
        "@maven//:org_antlr_antlr4_runtime",
    ],
)

kt_jvm_library(
    name = "language_kind",
    srcs = ["LanguageGlsl.kt"],
    deps = [
        "//src/org/perses/program:language_kind",
    ],
)
