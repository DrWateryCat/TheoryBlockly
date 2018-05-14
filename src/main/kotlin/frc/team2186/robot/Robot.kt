package frc.team2186.robot

import edu.wpi.first.wpilibj.IterativeRobot
import edu.wpi.first.wpilibj.Joystick
import frc.team2186.robot.lib.server.server

class Robot: IterativeRobot() {
    val joystick = Joystick(0)
    override fun robotInit() {
        Drive
        server()
    }

    override fun autonomousPeriodic() {
        Drive.update()
    }
    override fun teleopPeriodic() {
        val x = joystick.getRawAxis(0)
        val y = joystick.getRawAxis(1)
        val twist = joystick.getRawAxis(2)

        Drive.run {
            xSpeed = x
            ySpeed = y
            twistSpeed = twist
        }

        Drive.update()
    }
}