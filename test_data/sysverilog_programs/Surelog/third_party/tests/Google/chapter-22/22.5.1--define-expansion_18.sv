/*
:name: 22.5.1--define_expansion_18
:description: Test
:should_fail_because: parantheses required
:tags: 22.5.1
:type: preprocessing
*/
`define MACRO3(a=5, b=0, c="C") initial $display(a,,b,,c);
module top ();
`MACRO3
endmodule
