
import com.example.api.spring_boot_kotlin_service.dto.UserDto
import com.example.api.spring_boot_kotlin_service.dto.UserRoleDto
import com.example.api.spring_boot_kotlin_service.model.User

fun User.toDto(): UserDto {

    val dto = UserDto(
        userId = id!!,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
        userRoles = userRoles.map { userRole ->
            UserRoleDto(
                userRole.role.id!!,
                userRole.role.codeName,
            )
        }
    )
    return dto
}