package me.illumination

object Day07 {
  def main(): Unit =
    val ss = FileReaderUtils.readLines("day07.txt")
    solve(ss)
    solve2(ss)

  def solve(ss: List[String]): Unit =
    val hands = ss.map(Hand.apply).sortBy(h => h.ht)(HandTypeOrdering)
    println(hands)
    println(hands.zip(1 to hands.size).foldLeft(0L)((acc, t) => acc + t._1.bid * t._2))

  def solve2(ss: List[String]): Unit =
    val hands = ss.map(Hand.applyWildCard).sortBy(h => h.ht)(HandTypeWildCardOrdering)
    println(hands)
    println(hands.zip(1 to hands.size).foldLeft(0L)((acc, t) => acc + t._1.bid * t._2))

  case class Hand(val ht: HandType, val bid: Int)

  object Hand:
    def apply(s: String): Hand =
      val xs = s.split("\\s+")
      Hand(HandType(xs(0)), xs(1).toInt)

    def applyWildCard(s: String): Hand =
      val xs = s.split("\\s+")
      Hand(HandType.applyWildCard((xs(0))), xs(1).toInt)

  enum HandType(val cards: String):
    case FiveOfAKind(s: String) extends HandType(s)
    case FourOfAKind(s: String) extends HandType(s)
    case FullHouse(s: String) extends HandType(s)
    case ThreeOfAKind(s: String) extends HandType(s)
    case TwoPair(s: String) extends HandType(s)
    case OnePair(s: String) extends HandType(s)
    case HighCard(s: String) extends HandType(s)

  object HandTypeOrdering extends Ordering[HandType]:
    val map: Map[Char, Int] = Map(
      'A' -> 14,
      'K' -> 13,
      'Q' -> 12,
      'J' -> 11,
      'T' -> 10,
      '9' -> 9,
      '8' -> 8,
      '7' -> 7,
      '6' -> 6,
      '5' -> 5,
      '4' -> 4,
      '3' -> 3,
      '2' -> 2,
    )

    private def compareLists(xs: List[Int], ys: List[Int]): Int =
      if xs == ys then 0
      else xs.zip(ys).map(t => t._1 - t._2).find(d => d != 0).get

    def compare(x: HandType, y: HandType): Int =
      if x.ordinal != y.ordinal then y.ordinal - x.ordinal
      else compareLists(x.cards.map(ch => map(ch)).toList, y.cards.map(ch => map(ch)).toList)

  object HandTypeWildCardOrdering extends Ordering[HandType]:
    val map: Map[Char, Int] = Map(
      'A' -> 14,
      'K' -> 13,
      'Q' -> 12,
      'T' -> 10,
      '9' -> 9,
      '8' -> 8,
      '7' -> 7,
      '6' -> 6,
      '5' -> 5,
      '4' -> 4,
      '3' -> 3,
      '2' -> 2,
      'J' -> 1,
    )

    private def compareLists(xs: List[Int], ys: List[Int]): Int =
      if xs == ys then 0
      else xs.zip(ys).map(t => t._1 - t._2).find(d => d != 0).get

    def compare(x: HandType, y: HandType): Int =
      if x.ordinal != y.ordinal then y.ordinal - x.ordinal
      else compareLists(x.cards.map(ch => map(ch)).toList, y.cards.map(ch => map(ch)).toList)

  object HandType:
    def apply(cards: String): HandType =
      val map = cards.groupMap(identity)(_ => 1).view.mapValues(_.sum).toMap
      if map.size == 1 then FiveOfAKind(cards)
      else if map.values.toList.sorted == List(1, 4) then FourOfAKind(cards)
      else if map.values.toList.sorted == List(2, 3) then FullHouse(cards)
      else if map.values.toList.sorted == List(1, 1, 3) then ThreeOfAKind(cards)
      else if map.values.toList.sorted == List(1, 2, 2) then TwoPair(cards)
      else if map.values.toList.sorted == List(1, 1, 1, 2) then OnePair(cards)
      else HighCard(cards)

    def applyWildCard(cards: String): HandType =
      val map = cards.filter(ch => ch != 'J').groupMap(identity)(_ => 1).view.mapValues(_.sum).toMap
      val jcount = 5 - map.values.toList.sum
      if jcount == 0 then apply(cards)
      else if jcount == 5 || jcount == 4 then FiveOfAKind(cards)
      else if jcount == 3 then
        if map.size == 1 then FiveOfAKind(cards)
        else FourOfAKind(cards)
      else if jcount == 2 then
        if map.size == 3 then ThreeOfAKind(cards)
        else if map.size == 2 then FourOfAKind(cards)
        else FiveOfAKind(cards)
      else
        if map.size == 4 then OnePair(cards)
        else if map.size == 3 then ThreeOfAKind(cards)
        else if map.size == 2 && map.values.toList.sorted == List(1, 3) then FourOfAKind(cards)
        else if map.size == 2 && map.values.toList.sorted == List(2, 2) then FullHouse(cards)
        else FiveOfAKind(cards)
}
