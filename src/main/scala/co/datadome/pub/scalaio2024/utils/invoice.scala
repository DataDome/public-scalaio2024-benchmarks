package co.datadome.pub.scalaio2024.utils

import scala.util.Random

final case class Invoice(id: Int, name: String, quantity: Int) extends Identified {
  override def hashCode(): Int = id
}

object Invoice {
  given Ordering[Invoice] with {
    def compare(x: Invoice, y: Invoice): Int = {
      if (x.id < y.id) -1
      else if (x.id > y.id) 1
      else 0
    }
  }

  def random(id: Int)(using rand: Random): Invoice = Invoice(id, rand.nextSimpleString(20), rand.nextInt(100))
}


final case class BadInvoice(id: Int, name: String, quantity: Int) extends Identified {
  override def hashCode(): Int = name.hashCode
}

object BadInvoice {
  given Ordering[BadInvoice] with {
    def compare(x: BadInvoice, y: BadInvoice): Int = {
      if (x.id < y.id) -1
      else if (x.id > y.id) 1
      else 0
    }
  }

  def random(id: Int)(using rand: Random): BadInvoice = BadInvoice(id, rand.nextSimpleString(20), rand.nextInt(100))
}
