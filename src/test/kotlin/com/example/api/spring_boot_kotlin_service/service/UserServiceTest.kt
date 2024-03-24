package com.example.api.spring_boot_kotlin_service.service

import com.example.api.spring_boot_kotlin_service.exception.UserCreationFailedException
import com.example.api.spring_boot_kotlin_service.exception.UserNotFoundException
import com.example.api.spring_boot_kotlin_service.fixture.UserFixture.Companion.roles
import com.example.api.spring_boot_kotlin_service.fixture.UserFixture.Companion.user
import com.example.api.spring_boot_kotlin_service.fixture.UserFixture.Companion.userDto
import com.example.api.spring_boot_kotlin_service.model.User
import com.example.api.spring_boot_kotlin_service.repository.RoleRepository
import com.example.api.spring_boot_kotlin_service.repository.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

     private lateinit var userService: UserService

     @Mock
     lateinit var roleRepository: RoleRepository

    @Mock
    lateinit var userRepository: UserRepository

    @Captor
    val userCaptor: ArgumentCaptor<User> =
        ArgumentCaptor.forClass(User::class.java)

    @BeforeEach
    fun setup() {
        this.userService = UserService(
            roleRepository,
            userRepository
        )
    }

    @Nested
    inner class CreateUserTest{

        @Test
        fun createUser(){


            whenever(roleRepository.findByCodeNameIn(userDto.userRoles.map { it.userRoleName.name })).thenReturn(roles)

            whenever(userRepository.save(userCaptor.capture())).thenReturn(user)

            val response  = userService.createUser(userDto = userDto)

            val actualInvocationOfSave = userCaptor.value

            Assertions.assertNotNull(actualInvocationOfSave.id)
            Assertions.assertNotNull(response.userId)

            Assertions.assertEquals(userDto.email, response.email, actualInvocationOfSave.email )
            Assertions.assertEquals(userDto.firstName,response.firstName, actualInvocationOfSave.firstName)
            Assertions.assertEquals(userDto.lastName, response.lastName, actualInvocationOfSave.lastName)
            Assertions.assertEquals(userDto.phone, response.phone, actualInvocationOfSave.phone)
            Assertions.assertTrue(response.userRoles.size==1)
            Assertions.assertNotNull(roles[0].id, actualInvocationOfSave.id)
            Assertions.assertEquals(userDto.userRoles[0].userRoleName.name, response.userRoles[0].userRoleName.name, roles[0].codeName.name)


        }

        @Test
        fun inCaseOfExceptionRaiseCustomException(){

            val requestRoles = userDto.userRoles.map { it.userRoleName.name }

            try {
                whenever(roleRepository.findByCodeNameIn(requestRoles)).thenReturn(roles)

                whenever(userRepository.save(userCaptor.capture())).thenThrow(RuntimeException())

                userService.createUser(userDto = userDto)
            }catch (ex: Exception){
                Assertions.assertEquals(UserCreationFailedException::class.java,ex.javaClass)
            }

            Mockito.verify(roleRepository, times(1)).findByCodeNameIn(requestRoles)
            Mockito.verify(userRepository, times(1)).save(userCaptor.capture())
        }
    }

    @Nested
    inner class GetUserByIdTest{

        @Test
        fun getUserById(){

            val requestUserId = "test-user-id"
            whenever(userRepository.findById("test-user-id")).thenReturn(Optional.of(user))

            val response  = userService.getUserById(requestUserId)

            Assertions.assertNotNull(response.userId)

            Assertions.assertEquals(user.email, response.email, response.email )
            Assertions.assertEquals(user.firstName,response.firstName, response.firstName)
            Assertions.assertEquals(user.lastName, response.lastName, response.lastName)
            Assertions.assertEquals(user.phone, response.phone, response.phone)
            Assertions.assertTrue(response.userRoles.size==1)

        }

        @Test
        fun inCaseUserNotFound(){
            val requestUserId = "wrong-user-id"
            try {

                whenever(userRepository.findById(requestUserId)).thenReturn(Optional.empty())

                userService.getUserById(requestUserId)

            }catch (ex: Exception){
                Assertions.assertEquals(UserNotFoundException::class.java,ex.javaClass)
            }

            Mockito.verify(userRepository, times(1)).findById(requestUserId)
        }
    }

}