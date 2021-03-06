package org.zenmode.commands.result

import org.scalatest.{ParallelTestExecution, FunSuite}
import org.scalatest.matchers.ShouldMatchers

class ResultSeqSuite extends FunSuite with ShouldMatchers with ParallelTestExecution {

  private val successRes = SingleResult(ExitCodes.success, "Success output")
  private val failRes = SingleResult(ExitCodes.fail, "Fail output")

  test("Exit code should equal success if all results succeed") {
    val resultSeq = ResultSeq(Seq(successRes, successRes))
    resultSeq.exitCode should equal (ExitCodes.success)
  }

  test("Exit code should equal fail if some of results fail") {
    val resultSeq = ResultSeq(Seq(successRes, failRes))
    resultSeq.exitCode should equal (ExitCodes.fail)
  }

  test("Stdout should be all stdout's appended") {
    val resultSeq = ResultSeq(Seq(successRes, failRes))
    resultSeq.stdoutAsString should equal (successRes.stdoutAsString + failRes.stdoutAsString)
  }

  test("Stderr should be all stderr's appended") {
    val resultSeq = ResultSeq(Seq(successRes, failRes))
    resultSeq.stderrAsString should equal (successRes.stderrAsString + failRes.stderrAsString)
  }

  test("Should succeed if all commands are succeeded") {
    val resultSeq = ResultSeq(Seq(successRes, successRes))
    resultSeq.succeeded should be (true)
  }

  test("Should add two sequences") {
    val sumOfSeqs = ResultSeq(successRes) ++ ResultSeq(failRes)
    sumOfSeqs.results should equal (Seq(successRes, failRes))
  }
}
