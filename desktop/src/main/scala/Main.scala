package ludumdare32

import com.badlogic.gdx.backends.lwjgl._

object Main extends App {
    val cfg = new LwjglApplicationConfiguration
    cfg.title = "Ludum Dare Skeleton"
    cfg.height = 480
    cfg.width = 800
    cfg.forceExit = false
    new LwjglApplication(new LudumDareSkeleton, cfg)
}
