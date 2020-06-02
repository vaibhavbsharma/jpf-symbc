#!/usr/bin/perl

use strict;

die "Usage: extract-detailed-results.pl <path-to-java-ranger-logs-directory> <output-csv-filename>"
  unless @ARGV == 2;
my($logs_dir, $output_file) = @ARGV;

opendir(DIR, $logs_dir) or die $!;
if (-e $output_file) {
    print "deleting previous csv file $output_file\n";
    unlink $output_file or die "failed to delete previous csv $output_file";
}
my @expected_logs =
  (["wbs.5step.mode1.log",  "wbs", 1, 15],
  ["wbs.10step.mode2.log",  "wbs", 2, 30],
  ["wbs.10step.mode3.log",  "wbs", 3, 30],
  ["wbs.10step.mode4.log",  "wbs", 4, 30],
  ["wbs.10step.mode5.log",  "wbs", 5, 30],
  ["tcas.2step.mode1.log",  "tcas", 1, 24],
  ["tcas.10step.mode2.log",  "tcas", 2, 24],
  ["tcas.10step.mode3.log",  "tcas", 3, 24],
  ["tcas.10step.mode4.log",  "tcas", 4, 24],
  ["tcas.10step.mode5.log",  "tcas", 5, 24],
  ["replace11.mode1.log",  "replace", 1, 11],
  ["replace11.mode2.log",  "replace", 2, 11],
  ["replace11.mode3.log",  "replace", 3, 11],
  ["replace11.mode4.log",  "replace", 4, 11],
  ["replace11.mode5.log",  "replace", 5, 11],
  ["DumpXML.7sym.mode1.log",  "NanoXML", 1, 7],
  ["DumpXML.7sym.mode2.log",  "NanoXML", 2, 7],
  ["DumpXML.7sym.mode3.log",  "NanoXML", 3, 7],
  ["DumpXML.7sym.mode4.log",  "NanoXML", 4, 7],
  ["DumpXML.7sym.mode5.log",  "NanoXML", 5, 7],
  ["siena.6.mode1.log",  "Siena", 1, 6],
  ["siena.6.mode5.log",  "Siena", 5, 6],
  ["schedule.mode1.log",  "Schedule", 1, 3],
  ["schedule.mode2.log",  "Schedule", 2, 3],
  ["schedule.mode3.log",  "Schedule", 3, 3],
  ["schedule.mode4.log",  "Schedule", 4, 3],
  ["schedule.mode5.log",  "Schedule", 5, 3],
  ["printtokens.5sym.mode1.log",  "PrintTokens2", 1, 5],
  ["printtokens.5sym.mode2.log",  "PrintTokens2", 2, 5],
  ["printtokens.5sym.mode3.log",  "PrintTokens2", 3, 5],
  ["printtokens.5sym.mode4.log",  "PrintTokens2", 4, 5],
  ["printtokens.5sym.mode5.log",  "PrintTokens2", 5, 5],
  ["ApacheCLI.5_1sym.mode1.log",  "ApacheCLI", 1, 6],
  ["ApacheCLI.5_1sym.mode2.log",  "ApacheCLI", 2, 6],
  ["ApacheCLI.5_1sym.mode3.log",  "ApacheCLI", 3, 6],
  ["ApacheCLI.5_1sym.mode4.log",  "ApacheCLI", 4, 6],
  ["ApacheCLI.5_1sym.mode5.log",  "ApacheCLI", 5, 6],
  ["merarbiter.6step.mode1.log",  "MerArbiter", 1, 24],
  ["merarbiter.6step.mode2.log",  "MerArbiter", 2, 24],
  ["merarbiter.6step.mode3.log",  "MerArbiter", 3, 24],
  ["merarbiter.6step.mode4.log",  "MerArbiter", 4, 24],
  ["merarbiter.6step.mode5.log",  "MerArbiter", 5, 24],
  );

my $csv_header = "Benchmark name, # sym inputs, java ranger mode,total runtime (msec), static analysis time (msec), dynamic symbolic execution runtime (msec), # execution paths, # queries to solver, total solver time (msec), solver parsing time (msec, solver cleanup time (msec), # distinct regions that were instaniated and successfully used, # distinct regions with conc. cond., # distinct regions with sym. cond. that we failed to instantiate, # distinct failed regions for fields, # distinct failed regions for SPF cases, # distinct failed regions due to missing method summary, # distinct failed regions for other reasons, # higher order regions used, # distinct regions that were encountered, # successful region instantiations, # total failed instantiations, # concrete condition instantiations, # aborted instantiations due to field references, # aborted instantiations due to SPF case instructions, # aborted instantiations due to missing method summary, # aborted instantiations due to other reasons, # regions summarized, # methods summarized, max branch depth for regions, max execution path count for regions, avg. execution path count for regions, # total exceptions thrown during static analysis only, # total exceptions thrown during instantiation only, # total exceptions potentially thrown during unknown  phase (could be either of the two phases)";
my $not_finished = "not-finished, " x 35;
open (OUTPUT, ">>$output_file") or die "failed to open $output_file";
print OUTPUT "$csv_header\n";
close OUTPUT;
for (my $i = 0; $i < 42; $i++) { 
    my($file, $benchmark, $mode, $nsym_inputs) = @{$expected_logs[$i]};
    print "extracting logs from $file\n";
    print "benchmark = $benchmark, nsym_inputs = $nsym_inputs, mode = $mode\n";
    unless(open(LOG, "<", "$logs_dir/$file")) {
	print "failed to open log file $logs_dir/$file\n";
	open (OUTPUT, ">>$output_file") or die "failed to open $output_file";
	print OUTPUT "$benchmark, $nsym_inputs, $mode, $not_finished\n";
	close OUTPUT;
	next;
    }
    my $save_metrics = 0;
    while(<LOG>) {
	# print $_;
	chomp $_;
	if (/^Metrics Vector:$/) { $save_metrics = 1; next;}
	if ($save_metrics == 1) { 
	    open (OUTPUT, ">>$output_file") or die "failed to open $output_file";
	    print "$_\n"; print OUTPUT "$benchmark, $nsym_inputs, $mode, $_\n"; $save_metrics = 0; 
	    close OUTPUT;
	};
    }
}


# my $input_len = 256;
# 
# my @crash_cond_opt = ("-check-for-ret-addr-overwrite", "-disqualify-path-on-ret-addr-overwrite",
# 		      "-check-condition-at", "0x08048da9:R_EBX:reg32_t>=\$0x007f:reg32_t", # prevents an out-of-bounds write in cgc_freaduntil to buf
# 		      "-disqualify-path-on-nonfalse-cond",
# 		      "-tracepoint",  "0x08048de8:R_EAX:reg32_t", # prints number of characters read by cgc_freaduntil 
# 		      "-tracepoint", "0x08048d97:R_EAX:reg32_t", # prints value returned from the first call to cgc__getc in cgc_freaduntil
# 		      "-tracepoint", "0x08048dc7:R_EAX:reg32_t", # prints value returned from the second call to cgc__getc in cgc_freaduntil
# 		      "-tracepoint", "0x08048e63:R_EBX:reg32_t" # prints value returned by cgc_receive before it gets translated
#     );
# 
# my $freaduntil_delim_neg1_je_eip = "0x08048d9f";
# my $freaduntil_delim_je_eip = "0x08048da3";
# my @branch_pref_opts = ("-branch-preference","$freaduntil_delim_je_eip:0",
# 			"-branch-preference","$freaduntil_delim_neg1_je_eip:0");
# 
# my $tests_file_prefix = "input_ce";
# my $invalid_tests_file_prefix = "invalid_input_ce";
# my $path_depth_limit = 300;
# # my $iteration_limit = 10000;
# my $adapter_score = 0;
# 
# my $region_limit = 936;
# my $reg_size = $arch eq "x64" ? "reg64_t" : "reg32_t";
# my $sane_addr = 0x42420000;  # starting sane address also assumed in SRFM#region_for (SRFM.ml line 873)
# my @fuzzball_extra_args_arr;
# my ($numTests,$numInvalidTests)=(0,0);
# my $fuzzball="fuzzball";
# my $stp="stp";
# 
# my $pwd = $ENV{PWD};
# 
# my $f1_completed_count = 0;
# my $iteration_count = 0;
# 
# # Try to figure out the code and data addresses we need based on
# # matching the output of "nm" and "objdump". Not the most robust
# # possible approach.
# 
# my $fuzz_start_addr = "0x" . substr(`nm $bin | fgrep " T main"`, 0, $arch eq "x64" ? 16 : 8);
# 
# my ($obj_start,$obj_end) = $arch eq "x64" ? (2,6) : (1,7);
# 
# 
# my @symbolic_arg_opts = ("-symbolic-stdin-concrete-size", "-replace-stdin-with-zero");
# 			 # "-input-region-sympresuf", sprintf("stdin:%d:%d:%d:0xa", $input_len, $sym_prefix_size, $sym_suffix_size));
#     
# my $first_libc_read_call_addr = 
#     (hex "0x" . substr(`objdump -dr $bin | grep 'call.*<__libc_read>' | head -n 1`, $obj_start, $obj_end));
# 
# my $repair_frag_start = "0x0804c2ac";
# my $repair_frag_end = "0x0804c2d4";
# 
# my @repair_opts = ( # TODO automate the inference of this target fragment selection
#     "-repair-frag-start", $repair_frag_start,  
#     "-repair-frag-end", $repair_frag_end);
# 
# print "fuzzball path = $fuzzball\n";
# print "stp path = $stp\n";
# print "fuzz-start-addr : $fuzz_start_addr\n";
# print "first_libc_read_call_addr : " . sprintf("0x%016x\n", $first_libc_read_call_addr);
# 
# # Field [0]: field name
# # Field [1]: Vine type
# # Field [2]: printf format for the string form
# 
# my @adapter_location_fields = (["repair_EIP", "reg64_t", "%016x"]);
# 
# my @argsub_fields =
#   (["a_is_const",  "reg1_t", "%01x"],
#    ["a_val",      $reg_size, "%016x"],
#    ["b_is_const",  "reg1_t", "%01x"],
#    ["b_val",      $reg_size, "%016x"],
#    ["c_is_const",  "reg1_t", "%01x"],
#    ["c_val",      $reg_size, "%016x"],
#    ["d_is_const",  "reg1_t", "%01x"],
#    ["d_val",      $reg_size, "%016x"],
#    ["e_is_const",  "reg1_t", "%01x"],
#    ["e_val",      $reg_size, "%016x"],
#    ["f_is_const",  "reg1_t", "%01x"],
#    ["f_val",      $reg_size, "%016x"],
# );
# 
# my @ret_fields =  
# (
#    ["ret_type",  "reg8_t", "%01x"],
#    ["ret_val",   $reg_size, "%016x"],
# );
# 
# 
# my $fnargs = 4;
# splice(@argsub_fields, 2 * $fnargs);
# my @fields = (@argsub_fields, @adapter_location_fields);
# 
# my @solver_opts = ("-solver", "smtlib-batch", "-solver-path", $stp
# 		    # , "-save-solver-files"
# 		   , "-solver-timeout",5,"-timeout-as-unsat"
#     );
# 
# my @synth_opt = ("-synthesize-repair-adapter",
# 		 join(":", "simple", $fnargs));
# 
# my @synth_ret_opt = ("-synthesize-repair-return-adapter",
# 		     join(":", "return-typeconv", 0)); # not applying return-typeconv adapter that uses callee arguments
# 
# print "synth_ret_opt = @synth_ret_opt\n";
# 
# my @verbose_1_opts = (
#     "-trace-repair",
#     "-trace-conditions",
#     "-trace-decisions",
#     "-trace-adapter",
#     "-trace-sym-addr-details",
#     "-trace-sym-addrs",
#     "-trace-syscalls",
#     "-trace-temps",
#     "-trace-memory-snapshots",
#     "-trace-tables",
#     "-trace-binary-paths-bracketed",
#     "-trace-solver",
#     "-trace-regions");
# 
# my @verbose_2_opts = (@verbose_1_opts,
# 		      "-trace-offset-limit",
# 		      "-trace-basic",
# 		      "-trace-eip",
# 		      "-trace-registers",
# 		      # "-trace-stmts",
# 		      "-trace-insns",
# 		      "-trace-loads",
# 		      "-trace-stores");
# my @verbose_opts = ($verbose == 1) ? @verbose_1_opts : (($verbose == 2) ? @verbose_2_opts : ());
# 
# 
# my @common_opts = (
#     "-check-store-sequence",
#     "-match-every-nonlocal-f2-write",
#     "-restrict-reads-to-N-bytes", 1,
#     "-tables-as-arrays",
#     "-stdin-replay-file", "/export/scratch/vaibhav/cb-multios/ShoutCTF-read-pkts",
#     "-stdin-replay-file-target-frag-offset", 42, # got this number by subtracting the total number of bytes read in the target fragment from the total number of bytes read by the binary - 1
#     "-skip-output-concretize",
#     @crash_cond_opt,
#     "-no-fail-on-huer",
#     "-return-zero-missing-x64-syscalls",
#     "-region-limit", $region_limit,
#     "-trace-iterations", "-trace-assigns", "-solve-final-pc",
#     "-trace-stopping",
#     "-table-limit","12",
#     "-omit-pf-af",
#     # "-match-syscalls-in-addr-range", # option isn't needed since we're already providing the repair-frag-start and repair-frag-end
#     # those serve as the starting and ending points where syscalls should be matched. We're asking FuzzBALL to match syscalls and their args
#     # by not giving the -dont-compare-linux-syscalls option.
#     # $cgc_check_call_addr.":".($cgc_check_call_addr+7),
#     "-random-seed", int(rand(10000000)),
#     "-nonzero-divisors",
#     # "-dont-compare-memory-sideeffects",
#     "-f1-iteration-limit", 256,
#     "-f2-iteration-limit", 256,
#     @repair_opts);
# 
# my @const_bounds_ec = ();
# if($const_lb <= $const_ub) {
#     for (my $i=0; $i<$fnargs; $i++) {
# 	my $n = chr(97 + $i);
# 	my $s1='';
# 	my $s2='';
# 	if($fnargs != 0) {
# 	    $s1 = sprintf("%s_is_const:reg1_t==0:reg1_t | %s_val:%s>=\$0x%Lx:%s",$n,$n,$reg_size,$const_lb,$reg_size);
# 	    $s2 = sprintf("%s_is_const:reg1_t==0:reg1_t | %s_val:%s<=\$0x%Lx:%s",$n,$n,$reg_size,$const_ub,$reg_size);
# 	}
# 	else {
# 	    $s1 = sprintf("%s_val:%s>=\$0x%016x:%s",$n,$reg_size,$const_lb,$reg_size);
# 	    $s2 = sprintf("%s_val:%s<=\$0x%016x:%s",$n,$reg_size,$const_ub,$reg_size);
# 	}
# 	push @const_bounds_ec, ("-extra-condition", $s1);
# 	push @const_bounds_ec, ("-extra-condition", $s2);
# 	#push @const_bounds_ec, ('-extra-condition '.$n.'_val:'.$reg_size.'<=$'.$const_ub.':'.$reg_size);
#     }
# }
# 
# #print "const_bounds_ec = @const_bounds_ec\n";
# 
# # http://stackoverflow.com/questions/17860976/how-do-i-output-a-string-of-hex-values-into-a-binary-file-in-perl
# sub generate_new_file
# {
#     my $fname = shift(@_);
#     my $aref = shift(@_);
# 
#     open(BIN, ">", $fname) or die;
#     binmode(BIN);
# 
#     for (my $i = 0; $i < @$aref; $i += 2)
#     {
# 	my ($hi, $lo) = @$aref[$i, $i+1];
# 	print BIN pack "H*", $hi.$lo;
#     }
#     close(BIN);
# }
# 
# sub create_input_ce_file {
#     my ($input_ce_ref) = shift(@_);
#     my ($file_name) = shift(@_);
#     my @input_ce= @{ $input_ce_ref};
#     my $input_ce_contents="";
#     for my $i (0 .. ($input_len-1)) {
# 	$input_ce_contents .= sprintf("%02x", $input_ce[$i]);
#     }
#     printf("input_ce_contents = $input_ce_contents\n");;
#     my @data_ary = split //, $input_ce_contents;
#     generate_new_file($file_name, \@data_ary);
# }
# 
# sub write_wrong_adapters {
#     my ($file_name) = shift(@_);
#     my ($wrong_adapters_ref) = shift(@_);
#     my @wrong_adapters = @{ $wrong_adapters_ref};
#     open (FILE, "> $file_name") || die "problem opening $file_name\n";
#     for my $i (0 .. $#wrong_adapters) {
# 	print "$i wrong adapter: $wrong_adapters[$i]\n";
# 	print FILE $wrong_adapters[$i] . "\n";
#     }
#     close FILE;
# } 
# 
# # Given the specification of an adapter, execute it with symbolic
# # inputs to either check it, or produce a counterexample.
# sub check_adapter {
#     my($adapt,$ret_adapt) = (@_);
#     # print "checking arg-adapter = @$adapt ret-adapter = @$ret_adapt\n";
#     my @conc_adapt = ();
#     if ($fnargs > 0) {
# 	for my $i (0 .. $#$adapt) {
# 	    my($name, $ty, $fmt) = @{$fields[$i]};
# 	    my $val = $adapt->[$i];
# 	    my $s = sprintf("%s:%s==0x$fmt:%s", $name, $ty, $val, $ty);
# 	    push @conc_adapt, ("-extra-condition", $s);
# 	}
#     }
#     my @conc_ret_adapt = ();
#     for my $i (0 .. $#$ret_adapt) {
#     	my($name, $ty, $fmt) = @{$ret_fields[$i]};
#     	my $val = $ret_adapt->[$i];
#     	my $s = sprintf("%s:%s==0x$fmt:%s", $name, $ty, $val, $ty);
#     	push @conc_ret_adapt, ("-extra-condition", $s);
#     }
#     my @args = ($fuzzball, "-linux-syscalls", "-arch", $arch, @symbolic_arg_opts, 
# 		# "-trace-detailed-range","$repair_frag_start-$repair_frag_end",
# 		$bin,
# 		# "-trace-eip",
# 		@solver_opts, "-fuzz-start-addr", $fuzz_start_addr,
# 		"-trace-regions", # do not turn off, necessary for finding the "Address <> is region <>" line in output below
# 		@verbose_opts,
# 		#"-narrow-bitwidth-cutoff","1", # I have no idea what this option does
# 		@synth_opt, @conc_adapt, @const_bounds_ec,
# 		($ret_adapter_on == 1 ? @synth_ret_opt : ()), 
# 		($ret_adapter_on == 1 ? @conc_ret_adapt : ()),
# 		#"-path-depth-limit", $path_depth_limit,
# 		# "-iteration-limit", $iteration_limit,
# 		#"-branch-preference", "$match_jne_addr:0",
# 		@branch_pref_opts,
# 		@common_opts,
# 		"--", $bin);
#     my @printable;
#     for my $a (@args) {
# 	if ($a =~ /[\s|<>]/) {
# 	    push @printable, "'$a'";
# 	} else {
# 	    push @printable, $a;
# 	}
#     }
#     if ($verbose != 0) { print "@printable\n"; }
#     open(LOG, "-|", @args);
#     my($matches, $fails) = (0, 0);
#     my(@ce, $this_ce);
#     my (@input_ce);
#     my(@arg_to_regnum, @regnum_to_arg, @fuzzball_extra_args, @regnum_to_saneaddr);
#     my(@region_contents);
#     $this_ce = 0;
#     my $f1_completed = 0;
#     $f1_completed_count = 0;
#     $iteration_count = 0;
#     while (<LOG>) {
# 	if ($_ eq "Match\n" ) { 
# 	    $matches++;
# 	} elsif (/^Iteration (.*):$/) {
# 	    $f1_completed = 0;
# 	    @arg_to_regnum = (0) x $fnargs;
# 	    @regnum_to_arg = (0) x 1000;
# 	    @regnum_to_saneaddr = (0) x ($fnargs+1);
# 	    my @tmp_reg_arr;
# 	    @region_contents = ();
# 	    # region_contents is row-indexed by argument number starting from 0
# 	    # but col-indexed from 1 up to region_limit
# 	    # this is because region_contents[i][0] indicates if a argument
# 	    # has a region assigned to it
# 	    for my $i (0 .. $region_limit+1) { push @tmp_reg_arr, 0; }
# 	    for my $i (0 .. $fnargs-1) { push @region_contents, [@tmp_reg_arr]; }
# 	    for my $i (0 .. $#region_contents) {
# 		for my $j (0 .. $#{$region_contents[$i]}) {
# 		    $region_contents[$i][$j]=0;
# 		}
# 	    }
# 	    $iteration_count++;
# 	} elsif ($_ eq "Completed f1\n") { # TODO make FB print "Completed f1"
# 	    $f1_completed = 1;
# 	    $f1_completed_count++;
# 	} elsif (($_ eq "Mismatch\n") or
# 		 (/^Stopping at null deref at (0x[0-9a-f]+)$/ and $f1_completed == 1) or
# 		 (/^Stopping at access to unsupported address at (0x[0-9a-f]+)$/ and $f1_completed == 1) or
# 		 (/^Stopping on disqualified path at (0x[0-9a-f]+)$/ and $f1_completed == 1) or 
# 		 (/^Disqualified path at (0x[0-9a-f]+)$/ and $f1_completed == 1)) {
# 	    $fails++;
# 	    $this_ce = 1;
# 	} elsif (/^Input vars: (.*)$/ and $this_ce) {
# 	    my $vars = $1;
# 	    @ce = (0) x $fnargs;
# 	    @input_ce = (0) x $input_len;
# 	    for my $v (split(/ /, $vars)) {
# 		if ($v =~ /^([a-f])=(0x[0-9a-f]+)$/) {
# 		    my $index = ord($1) - ord("a");
# 		    $ce[$index] = hex $2;
# 		    # printf("arg_to_regnum[$index] = %d\n",
# 		    # 	   $arg_to_regnum[$index]);
# 		    if ($arg_to_regnum[$index] != 0) {
# 			$ce[$index] = $sane_addr;
# 			$regnum_to_saneaddr[$arg_to_regnum[$index]] = $sane_addr;
# 			$sane_addr = $sane_addr + $region_limit;
# 		    }
# 		} elsif ($v =~ /^input0_([0-9]+)=(0x[0-9a-f]+)$/) {
# 		    $input_ce[$1] = hex $2;
# 		} elsif ($v =~ /^stdin_byte_0x([0-9a-f]+)=(0x[0-9a-f]+)$/) {
# 		    $input_ce[(hex $1)] = hex $2;
# 		}
# 	    }
# 	    for my $v (split (/ /, $vars)) {
# 		if($v =~ /^region_([0-9]+)_byte_0x([0-9a-f]+)=(0x[0-9a-f]+)$/) {
# 		    if ($verbose != 0) { print "region assignment $1 $2 $3 for arg $regnum_to_arg[$1]\n"; }
# 		    # $1 -> region number, starts with 1
# 		    # $2 -> offset within region, starts with 0
# 		    # $3 -> value to be set, any value
# 		    my $region_number = $1 + 0;
# 		    my $region_offset = hex $2;
# 		    $region_offset += 1; # because first value is 1 if region is used
# 		    my $arg_num = $regnum_to_arg[$region_number];
# 		    # printf("arg_num = %d, region_number = $region_number\n", $arg_num);
# 		    if($regnum_to_saneaddr[$region_number] != 0) {
# 			$region_contents[$arg_num][$region_offset]=$3;
# 			# printf("region_contents[$arg_num][$region_offset] = %s (%s), with saneaddr = 0x%x\n", 
# 			#        $region_contents[$arg_num][$region_offset], $3,
# 			#        $regnum_to_saneaddr[$1]);
# 		    }
# 		    else { # printf("cannot find regnum_to_saneaddr for $1\n"); 
# 		    }
# 		}
# 	    }
# 	    for my $i (0 .. $fnargs-1) {
# 		my $str_arg_contents="";
# 		for my $j (1 .. $region_limit) {
# 		    # printf("region_contents[$i][$j] = %s, sane_addr = 0x%x\n", 
# 		    # 	   $region_contents[$i][$j],
# 		    # 	   $regnum_to_saneaddr[$arg_to_regnum[$i]]);
# 		    my $byte="0x00";
# 		    if($region_contents[$i][0] == 1) {
# 			push @fuzzball_extra_args, "-store-byte";
# 			push @fuzzball_extra_args, 
# 			sprintf("0x%x=%s", 
# 				$regnum_to_saneaddr[$arg_to_regnum[$i]]+$j-1, 
# 				$region_contents[$i][$j]);
# 			# printf("pushed 0x%x=%s\n", 
# 			# 	$regnum_to_saneaddr[$arg_to_regnum[$i]]+$j-1, 
# 			# 	$region_contents[$i][$j]); 
# 			$str_arg_contents .= substr $region_contents[$i][$j], 2;
# 		    } else { 
# 			$str_arg_contents .= "00";
# 		    }
# 		}
# 		# printf("str_arg_contents = $str_arg_contents\n");;
# 		my @data_ary = split //, $str_arg_contents;
# 		# generate_new_file("str_arg${i}_$numTests", \@data_ary);
# 	    }
# 	    create_input_ce_file(\@input_ce, $tests_file_prefix . "" . $numTests);
# 	    $this_ce = 0;
# 	    if ($verbose != 0) { print "  $_"; }
# 	    last;
# 	} elsif (/Address [a-f]:${reg_size} is region ([0-9]+)/) {
# 	    my $add_line = $_;
# 	    my $add_var = -1;
# 	    for my $v (split(/ /, $add_line)) {
# 		if ($v =~ /^[a-f]:${reg_size}$/) { # matches argument name
# 		    $add_var = ord($v) - ord('a');
# 		    if ($verbose != 0) { printf("add_var = $add_var\n"); }
# 		} elsif ($v =~ /^[0-9]$/) { # matches region number
# 		    if ($add_var < $fnargs and $add_var >= 0) {
# 			$arg_to_regnum[$add_var] = $v-'0';
# 			# printf("arg_to_regnum[$add_var] = %d\n", 
# 			#        $arg_to_regnum[$add_var]);
# 			$regnum_to_arg[$v-'0'] = $add_var;
# 			# 1 indicates symbolic input created a region
# 			$region_contents[$add_var][0]=1;
# 		    }
# 		}
# 	    }
# 	} # elsif print strings used by eg/libc/exp-scripts/run-funcs.pl 
# 	elsif(/.*total query time = (.*)$/ || 
# 		/.*Query time = (.*) sec$/ || 
# 		/.*Starting new query.*$/) {
# 	    print "  $_";
# 	}
#  
# 	if ($verbose != 0) { print "  $_"; }
#     }
#     close LOG;
#     if ($matches == 0 and $fails == 0) {
# 	print "CounterExample search failed";
# 	die "Missing results from check run";
#     }
#     $numTests++;
#     if ($fails == 0) {
# 	return 1;
#     } else {
# 	return (0, [@input_ce], [@fuzzball_extra_args]);
#     }
# }
# 
# # Given a set of tests, run with the adapter symbolic to see if we can
# # synthesize an adapter that works for those tests.
# sub try_synth {
#     my($testsr, $_fuzzball_extra_args, $wrong_argsub_adapters_ref, $wrong_ret_adapters_ref) = @_;
#     my @fuzzball_extra_args = @{ $_fuzzball_extra_args };
#     my @wrong_argsub_adapters = @{ $wrong_argsub_adapters_ref };
#     my @wrong_ret_adapters = @{ $wrong_ret_adapters_ref };
#     open(TESTS, ">tests");
#     for my $t (@$testsr) {
# 	my @vals = (@$t, (0) x 6);
# 	splice(@vals, 6);
# 	my $test_str = join(" ", map(sprintf("0x%x", $_), @vals));
# 	print TESTS $test_str, "\n";
#     }
#     close TESTS;
#     my ($wrong_argsub_adapters_file, $wrong_ret_adapters_file) = ("wrong-argsub-adapters.lst", "wrong-ret-adapters.lst");
#     write_wrong_adapters($wrong_argsub_adapters_file, \@wrong_argsub_adapters);
#     if ($ret_adapter_on == 1) {write_wrong_adapters($wrong_ret_adapters_file,\@wrong_ret_adapters);}
#     
#     my @args = ($fuzzball, "-linux-syscalls", "-arch", $arch, $bin,
# 		# "-trace-detailed-range","$repair_frag_start-$repair_frag_end",
# 		@solver_opts, 
# 		@target_frag_call_insn_opt, 
# 		# "-dont-compare-linux-syscalls",
# 		"-fuzz-start-addr", $fuzz_start_addr,
# 		#tell FuzzBALL to run in adapter search mode, FuzzBALL will run in
# 		#counter example search mode otherwise
# 		"-adapter-search-mode",
# 		@verbose_opts,
# 		@synth_opt, @const_bounds_ec,
# 		($ret_adapter_on == 1 ? @synth_ret_opt : ()),
# 		#"-branch-preference", "$match_jne_addr:1",
# 		@fuzzball_extra_args,
# 		"-zero-memory",
# 		@common_opts,
# 		# "-extra-condition", "a_is_const:reg1_t==0x0:reg1_t", "-extra-condition", "a_val:reg32_t==0x00000000:reg32_t",,
# 		# "-extra-condition", "b_is_const:reg1_t==0x1:reg1_t", "-extra-condition", "b_val:reg32_t==0x00000080:reg32_t",,
# 		# "-extra-condition", "c_is_const:reg1_t==0x0:reg1_t", "-extra-condition", "c_val:reg32_t==0x00000002:reg32_t",,
# 		# "-extra-condition", "d_is_const:reg1_t==0x0:reg1_t", "-extra-condition", "d_val:reg32_t==0x00000003:reg32_t",,
# 		# "-extra-condition", "repair_EIP:reg64_t==0x0804c2cf:reg64_t",
# 		"-repair-tests-file", "$tests_file_prefix:$numTests",
# 		"-invalid-repair-tests-file", "$invalid_tests_file_prefix:$numInvalidTests",
# 		"-wrong-argsub-adapters-file", $wrong_argsub_adapters_file,
# 		($ret_adapter_on == 1 ? ("-wrong-ret-adapters-file", $wrong_ret_adapters_file) : ()), 
# 		"--", $bin);
#     
#     my @printable;
#     for my $a (@args) {
# 	if ($a =~ /[\s|<>]/) {
# 	    push @printable, "'$a'";
# 	} else {
# 	    push @printable, $a;
# 	}
#     }
#     if ($verbose != 0) { print "@printable\n"; }
#     open(LOG, "-|", @args);
#     my($success, %fields);
#     $success = 0;
#     while (<LOG>) {
# 	if ($_ eq "All tests succeeded!\n") {
# 	    $success = 1;
# 	} elsif (/^Disqualified path at (0x[0-9a-f]+)$/) {
# 	    $success = 0;
# 	} elsif (/^Input vars: (.*)$/ and $success) {
# 	    my $vars = $1;
# 	    %fields = ();
# 	    for my $v (split(/ /, $vars)) {
# 		$v =~ /^(\w+)=(0x[0-9a-f]+)$/
# 		  or die "Parse failure on variable assignment <$v>";
# 		$fields{$1} = hex $2;
# 	    }
# 	    if ($verbose != 0) { print "  $_"; }
# 	    last;
# 	} elsif (/^adapter_score = (.*)$/ and $success) {
# 	    $adapter_score = $1;
# 	} # elsif print strings used by eg/libc/exp-scripts/run-funcs.pl 
# 	elsif(/.*total query time = (.*)$/ || 
# 		/.*Query time = (.*) sec$/ || 
# 		/.*Starting new query.*$/) {
# 	    print "  $_";
# 	}
# 
# 	if ($verbose != 0) { print "  $_" unless /^Input vars:/; }
#     }
#     close LOG;
#     if (!$success) {
# 	print "Synthesis failure: seems the functions are not equivalent.\n";
# 	exit 2;
#     }
#     my @afields;
#     my @bfields;
#     for my $fr (@fields) {
# 	push @afields, $fields{$fr->[0]};
#     }
#     for my $fr (@ret_fields) {
# 	push @bfields, $fields{$fr->[0]};
# 	#print "try_synth: pushing $fr->[0] = $fields{$fr->[0]}\n";
#     }
#     return ([@afields],[@bfields]);
# }
# 
# # Given the specification of an adapter, execute it with symbolic
# # inputs to check if it violates a security property 
# sub verify_adapter {
#     my($adapt,$ret_adapt) = (@_);
#     my @conc_adapt = ();
#     if ($fnargs > 0) {
# 	for my $i (0 .. $#$adapt) {
# 	    my($name, $ty, $fmt) = @{$fields[$i]};
# 	    my $val = $adapt->[$i];
# 	    my $s = sprintf("%s:%s==0x$fmt:%s", $name, $ty, $val, $ty);
# 	    push @conc_adapt, ("-extra-condition", $s);
# 	}
#     }
#     my @conc_ret_adapt = ();
#     for my $i (0 .. $#$ret_adapt) {
#     	my($name, $ty, $fmt) = @{$ret_fields[$i]};
#     	my $val = $ret_adapt->[$i];
#     	my $s = sprintf("%s:%s==0x$fmt:%s", $name, $ty, $val, $ty);
#     	push @conc_ret_adapt, ("-extra-condition", $s);
#     }
#     my @args = ($fuzzball, "-linux-syscalls", "-arch", $arch, @symbolic_arg_opts,
# 		"-dont-compare-linux-syscalls",
# 		$bin,
# 		@solver_opts, "-fuzz-start-addr", $fuzz_start_addr,
# 		"-trace-regions", # do not turn off, necessary for finding the "Address <> is region <>" line in output below
# 		@verbose_opts,
# 		#"-narrow-bitwidth-cutoff","1", # I have no idea what this option does
# 		"-verify-adapter",
# 		@synth_opt, @conc_adapt, @const_bounds_ec,
# 		($ret_adapter_on == 1 ? @synth_ret_opt : ()), 
# 		($ret_adapter_on == 1 ? @conc_ret_adapt: ()),
# 		#"-path-depth-limit", $path_depth_limit,
# 		# "-iteration-limit", $iteration_limit,
# 		#"-branch-preference", "$match_jne_addr:0",
# 		@branch_pref_opts,
# 		@common_opts,
# 		"--", $bin);
#     my @printable;
#     for my $a (@args) {
# 	if ($a =~ /[\s|<>]/) {
# 	    push @printable, "'$a'";
# 	} else {
# 	    push @printable, $a;
# 	}
#     }
#     if ($verbose != 0) { print "@printable\n"; }
#     open(LOG, "-|", @args);
#     my($matches, $fails) = (0, 0);
#     my(@ce, $this_ce);
#     my (@input_ce);
#     my(@arg_to_regnum, @regnum_to_arg, @fuzzball_extra_args, @regnum_to_saneaddr);
#     my(@region_contents);
#     $this_ce = 0;
#     $iteration_count = 0;
#     while (<LOG>) {
# 	if ($_ eq "Match\n" ) {
# 	    $matches++;
# 	} elsif (/^Iteration (.*):$/) {
# 	    @arg_to_regnum = (0) x $fnargs;
# 	    @regnum_to_arg = (0) x 1000;
# 	    @regnum_to_saneaddr = (0) x ($fnargs+1);
# 	    my @tmp_reg_arr;
# 	    @region_contents = ();
# 	    # region_contents is row-indexed by argument number starting from 0
# 	    # but col-indexed from 1 up to region_limit
# 	    # this is because region_contents[i][0] indicates if a argument
# 	    # has a region assigned to it
# 	    for my $i (0 .. $region_limit+1) { push @tmp_reg_arr, 0; }
# 	    for my $i (0 .. $fnargs-1) { push @region_contents, [@tmp_reg_arr]; }
# 	    for my $i (0 .. $#region_contents) {
# 		for my $j (0 .. $#{$region_contents[$i]}) {
# 		    $region_contents[$i][$j]=0;
# 		}
# 	    }
# 	    $iteration_count++;
# 	} elsif (($_ eq "Mismatch\n") or
# 		 (/^Stopping at null deref at (0x[0-9a-f]+)$/) or
# 		 (/^Stopping at access to unsupported address at (0x[0-9a-f]+)$/) or
# 		 (/^Stopping on disqualified path at (0x[0-9a-f]+)$/) or 
# 		 (/^Disqualified path at (0x[0-9a-f]+)$/)) {
# 	    $fails++;
# 	    $this_ce = 1;
# 	} elsif (/^Input vars: (.*)$/ and $this_ce) {
# 	    my $vars = $1;
# 	    @ce = (0) x $fnargs;
# 	    @input_ce = (0) x $input_len;
# 	    for my $v (split(/ /, $vars)) {
# 		if ($v =~ /^([a-f])=(0x[0-9a-f]+)$/) {
# 		    my $index = ord($1) - ord("a");
# 		    $ce[$index] = hex $2;
# 		    # printf("arg_to_regnum[$index] = %d\n",
# 		    # 	   $arg_to_regnum[$index]);
# 		    if ($arg_to_regnum[$index] != 0) {
# 			$ce[$index] = $sane_addr;
# 			$regnum_to_saneaddr[$arg_to_regnum[$index]] = $sane_addr;
# 			$sane_addr = $sane_addr + $region_limit;
# 		    }
# 		} elsif ($v =~ /^input0_([0-9]+)=(0x[0-9a-f]+)$/) {
# 		    $input_ce[$1] = hex $2;
# 		} elsif ($v =~ /^stdin_byte_0x([0-9a-f]+)=(0x[0-9a-f]+)$/) {
# 		    $input_ce[(hex $1)] = hex $2;
# 		}
# 	    }
# 	    for my $v (split (/ /, $vars)) {
# 		if($v =~ /^region_([0-9]+)_byte_0x([0-9a-f]+)=(0x[0-9a-f]+)$/) {
# 		    if ($verbose != 0) { print "region assignment $1 $2 $3 for arg $regnum_to_arg[$1]\n"; }
# 		    # $1 -> region number, starts with 1
# 		    # $2 -> offset within region, starts with 0
# 		    # $3 -> value to be set, any value
# 		    my $region_number = $1 + 0;
# 		    my $region_offset = hex $2;
# 		    $region_offset += 1; # because first value is 1 if region is used
# 		    my $arg_num = $regnum_to_arg[$region_number];
# 		    # printf("arg_num = %d, region_number = $region_number\n", $arg_num);
# 		    if($regnum_to_saneaddr[$region_number] != 0) {
# 			$region_contents[$arg_num][$region_offset]=$3;
# 			# printf("region_contents[$arg_num][$region_offset] = %s (%s), with saneaddr = 0x%x\n", 
# 			#        $region_contents[$arg_num][$region_offset], $3,
# 			#        $regnum_to_saneaddr[$1]);
# 		    }
# 		    else { # printf("cannot find regnum_to_saneaddr for $1\n"); 
# 		    }
# 		}
# 	    }
# 	    for my $i (0 .. $fnargs-1) {
# 		my $str_arg_contents="";
# 		for my $j (1 .. $region_limit) {
# 		    # printf("region_contents[$i][$j] = %s, sane_addr = 0x%x\n", 
# 		    # 	   $region_contents[$i][$j],
# 		    # 	   $regnum_to_saneaddr[$arg_to_regnum[$i]]);
# 		    my $byte="0x00";
# 		    if($region_contents[$i][0] == 1) {
# 			push @fuzzball_extra_args, "-store-byte";
# 			push @fuzzball_extra_args, 
# 			sprintf("0x%x=%s", 
# 				$regnum_to_saneaddr[$arg_to_regnum[$i]]+$j-1, 
# 				$region_contents[$i][$j]);
# 			# printf("pushed 0x%x=%s\n", 
# 			# 	$regnum_to_saneaddr[$arg_to_regnum[$i]]+$j-1, 
# 			# 	$region_contents[$i][$j]); 
# 			$str_arg_contents .= substr $region_contents[$i][$j], 2;
# 		    } else { 
# 			$str_arg_contents .= "00";
# 		    }
# 		}
# 		# printf("str_arg_contents = $str_arg_contents\n");;
# 		my @data_ary = split //, $str_arg_contents;
# 		# generate_new_file("str_arg${i}_$numInvalidTests", \@data_ary);
# 	    }
# 	    create_input_ce_file(\@input_ce, $invalid_tests_file_prefix . "" . $numInvalidTests);
# 	    $numInvalidTests++;
# 	    $this_ce = 0;
# 	    if ($verbose != 0) { print "  $_"; }
# 	    last;
# 	} elsif (/Address [a-f]:${reg_size} is region ([0-9]+)/) {
# 	    my $add_line = $_;
# 	    my $add_var = -1;
# 	    for my $v (split(/ /, $add_line)) {
# 		if ($v =~ /^[a-f]:${reg_size}$/) { # matches argument name
# 		    $add_var = ord($v) - ord('a');
# 		    if ($verbose != 0) { printf("add_var = $add_var\n"); }
# 		} elsif ($v =~ /^[0-9]$/) { # matches region number
# 		    if ($add_var < $fnargs and $add_var >= 0) {
# 			$arg_to_regnum[$add_var] = $v-'0';
# 			# printf("arg_to_regnum[$add_var] = %d\n", 
# 			#        $arg_to_regnum[$add_var]);
# 			$regnum_to_arg[$v-'0'] = $add_var;
# 			# 1 indicates symbolic input created a region
# 			$region_contents[$add_var][0]=1;
# 		    }
# 		}
# 	    }
# 	} # elsif print strings used by eg/libc/exp-scripts/run-funcs.pl 
# 	elsif(/.*total query time = (.*)$/ || 
# 		/.*Query time = (.*) sec$/ || 
# 		/.*Starting new query.*$/) {
# 	    print "  $_";
# 	}
#  
# 	if ($verbose != 0) { print "  $_"; }
#     }
#     close LOG;
#     if ($matches == 0 and $fails == 0) {
# 	print "VerifyAdapter failed";
# 	die "Missing results from check run";
#     }
#     if ($fails == 0) {
# 	return (1);
#     } else {
# 	return (0);
#     }
# }
# 
# # Main loop: starting with a stupid adapter and no tests, alternate
# # between test generation and synthesis.
# my $adapt = [(0) x @fields];
# my $ret_adapt = [(0) x @ret_fields];
# $adapt->[8]=$first_libc_read_call_addr;
#     
# # Setting up the default adapter to be the identity adapter
# if ($default_adapter_pref == 1) {
#     my $f1_narg_counter=0;
#     for my $i (0 .. (2*$fnargs-1)) {
# 	if ($i%2 == 1) {
# 	    $adapt->[$i] = $f1_narg_counter;
# 	    if ($f1_narg_counter < $fnargs-1) {
# 		$f1_narg_counter= $f1_narg_counter + 1;
# 	    }
# 	}
#     }
# }
# 
# # If outer function takes no arguments, then the inner function can only use constants
# if ($fnargs==0 || $default_adapter_pref == 0) {
#     for my $i (0 .. (2*$fnargs-1)) {
# 	if ($i%2 == 0) { # X_is_const field
# 	    $adapt->[$i] = 1;
# 	    $adapt->[$i+1] = 0;
# 	}
#     }
# }
# 
# # $adapt->[0]=0;
# # $adapt->[1]=0;
# # $adapt->[2]=1;
# # $adapt->[3]=128;
# # $adapt->[4]=0;
# # $adapt->[5]=2;
# # $adapt->[6]=0;
# # $adapt->[7]=3;
# # $adapt->[8]=0x0804c2cf;
# 
# print "default adapter = @$adapt ret-adapter = @$ret_adapt\n";
# my @tests = ();
# my $done = 0;
# my $start_time = time();
# my $reset_time = time();
# my $total_ce_time = 0;
# my $total_as_time = 0;
# my $total_va_time = 0;
# my $diff;
# my $diff1;
# # these two lists will contain comma-separated string representations of wrong adapters 
# my @wrong_argsub_adapters = ();
# my @wrong_ret_adapters = ();
# `rm str_arg*`;
# `rm input_ce*`;
# `rm invalid_input_ce*`;
# `rm -rf fuzzball-tmp-*`;
# `rm wrong-*-adapters.lst`;
# my ($ce_steps,$as_steps,$va_steps) = (0,0,0);
# while (!$done) {
#     my $adapt_s = join(",", @$adapt);
#     my $ret_adapt_s = join(",", @$ret_adapt);
#     print "Checking $adapt_s and $ret_adapt_s:\n";
#     my($res, $cer, $_fuzzball_extra_args) = check_adapter($adapt,$ret_adapt);
#     $diff = time() - $start_time;
#     $diff1 = time() - $reset_time;
#     print "elapsed time = $diff, last CE search time = $diff1\n";
#     $total_ce_time += $diff1;
#     $reset_time = time();
#     $ce_steps += 1;
#     if ($res) {
# 	print "Success!\n";
# 	print "Final test set:\n";
# 	for my $tr (@tests) {
# 	    print " $tr->[0], $tr->[1], $tr->[2] $tr->[3] $tr->[4] $tr->[5]\n";
# 	}
# 	my $verified="partial";
# 	if ($f1_completed_count == $iteration_count) {
# 	    $verified="complete";
# 	}
# 	my $scaled_adapter_score = ($adapter_score * $f1_completed_count) / $iteration_count;
# 	print "Final adapter is $adapt_s and $ret_adapt_s with $f1_completed_count,$iteration_count,$verified, adapter_score = $scaled_adapter_score ($adapter_score)\n";
# 	print "total_as_time = $total_as_time, total_ce_time = $total_ce_time, total_va_time = $total_va_time\n";
# 	print "(CE-steps,AS-steps,VA-steps)=($ce_steps,$as_steps,$va_steps)\n";
# 	$done = 1;
# 	last;
#     } else {
# 	push @fuzzball_extra_args_arr, @{ $_fuzzball_extra_args };
# 	my $ce_s = join(", ", @$cer);
# 	print "Mismatch on input $ce_s; adding as test\n";
# 	push @tests, [@$cer];
#     }
# 
#     my $verified_adapter = 0;
#     while (!$verified_adapter) {
# 		
# 	($adapt,$ret_adapt) = try_synth(\@tests, \@fuzzball_extra_args_arr, \@wrong_argsub_adapters, \@wrong_ret_adapters);
# 	print "Synthesized arg adapter ".join(",",@$adapt).
# 	    " and return adapter ".join(",",@$ret_adapt)."\n";
# 	$diff = time() - $start_time;
# 	$diff1 = time() - $reset_time;
# 	print "elapsed time = $diff, last AS search time = $diff1\n";
# 	$total_as_time += $diff1;
# 	$as_steps += 1;
# 	$reset_time = time();
# 
# 	$adapt_s = join(",", @$adapt);
# 	$ret_adapt_s = join(",", @$ret_adapt);
# 	print "Verifying $adapt_s and $ret_adapt_s:\n";
# 	($verified_adapter) = verify_adapter($adapt,$ret_adapt);
# 	$diff = time() - $start_time;
# 	$diff1 = time() - $reset_time;
# 	print "elapsed time = $diff, last VA search time = $diff1\n";
# 	$total_va_time += $diff1;
# 	$va_steps += 1;
# 	if (!$verified_adapter) {
# 	    push @wrong_argsub_adapters, $adapt_s;
# 	    push @wrong_ret_adapters, $ret_adapt_s;
# 	    print "Invalidated adapter: $adapt_s and $ret_adapt_s\n";
# 	} else {
# 	    print "Failed to invalidate adapter: $adapt_s and $ret_adapt_s\n";
# 	}
# 	$reset_time = time();
#     }
# }
