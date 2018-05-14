package frc.team2186.robot

import com.google.gson.JsonObject
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.Talon
import edu.wpi.first.wpilibj.drive.MecanumDrive

object Drive {
    private val frontLeft = Talon(0)
    private val frontRight = Talon(1)
    private val backLeft = Talon(2)
    private val backRight = Talon(3)

    private val mecanumDrive = MecanumDrive(frontLeft, backLeft, frontRight, backRight).apply {
        setMaxOutput(0.2)
    }

    var xSpeed = 0.0
    var ySpeed = 0.0
    var twistSpeed = 0.0

    fun update() {
        mecanumDrive.driveCartesian(xSpeed, ySpeed, twistSpeed)
    }

    val currentState: JsonObject
        get() = JsonObject().apply {
            addProperty("current_state", DriverStation.getInstance().run {
                when {
                    isAutonomous -> "Autonomous"
                    isDisabled -> "Disabled"
                    isOperatorControl -> "Teleop"
                    else -> "Unknown"
                }
            })
        }
}