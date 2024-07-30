package common

object Generic:
  import Tuple.*

  type IsTuple[X] = X match
    case Tuple => true
    case _     => false

  type Flatten[T <: Tuple] <: Tuple = T match
    case EmptyTuple => EmptyTuple
    case h *: t =>
      IsTuple[h] match
        case true  => Concat[Flatten[h], Flatten[t]]
        case false => h *: Flatten[t]

  type Normalize[T <: Tuple] = T match
    case EmptyTuple      => Unit
    case n *: EmptyTuple => n
    case _               => Tuple.Take[T, Tuple.Size[T]]

end Generic
