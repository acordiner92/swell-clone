package domain

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

import java.time.{LocalDateTime, OffsetDateTime}
import java.util.UUID

case class User(
                 id: UUID,
                 firstName: Option[String],
                 lastName: Option[String],
                 email: Option[String],
                 dateOfBirth: Option[String],
                 createdAt: LocalDateTime,
                 updatedAt: LocalDateTime
               )

object User {
  implicit val encoder: JsonEncoder[User] =
    DeriveJsonEncoder.gen[User]
  implicit val decoder: JsonDecoder[User] =
    DeriveJsonDecoder.gen[User]
}