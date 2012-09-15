package org.zenmode.cmd.command

import org.scalatest.{ParallelTestExecution, FunSuite}
import org.scalatest.matchers.ShouldMatchers
import org.mockito.Mockito._
import org.zenmode.cmd.executor.Executor
import org.zenmode.cmd.result.SingleResult

class CommandSequenceSuite extends FunSuite with ShouldMatchers with ParallelTestExecution {

  implicit val executor = mock(classOf[Executor])

  val firstCmd = Command("ls")
  val secondCmd = Command("cd")
  val cmdSeq = CommandSequence(firstCmd, secondCmd)

  test("Execution in order") {
    val order = inOrder(executor)

    cmdSeq.execute

    order.verify(executor).execute("ls", Nil, ".", Map.empty)
    order.verify(executor).execute("cd", Nil, ".", Map.empty)
  }

  test("Separate results") {
    val firstResult = SingleResult(0, "First output", "First errors")
    val secondResult = SingleResult(-1, "Second output", "Second result")

    when(executor.execute("ls", Nil, ".", Map.empty)).thenReturn(firstResult)
    when(executor.execute("cd", Nil, ".", Map.empty)).thenReturn(secondResult)

    val seqResult = cmdSeq.execute

    seqResult.results should equal (Seq(firstResult, secondResult))
  }
}