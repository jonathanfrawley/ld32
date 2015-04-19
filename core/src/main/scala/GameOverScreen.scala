package ludumdare32

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera}
import com.badlogic.gdx.{Gdx, Screen}

class GameOverScreen (game: LudumDareSkeleton) extends Screen {
  lazy val camera = new OrthographicCamera
  camera.setToOrtho(false, 800, 480)

  override def render(delta: Float): Unit = {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    camera.update()
    game.batch.setProjectionMatrix(camera.combined)

    game.batch.begin()
    game.font.draw(game.batch, "Game over", camera.viewportWidth/2, camera.viewportHeight/2)
    game.font.draw(game.batch, "Press enter to retry", camera.viewportWidth/2, camera.viewportHeight/3)
    game.batch.end()

    if(Gdx.input.isKeyPressed(Keys.ENTER)) {
      game.setScreen(new GameScreen(game))
      dispose()
    }
  }

  override def resize(width: Int, height: Int): Unit = {}

  override def hide(): Unit = {}

  override def dispose(): Unit = {}

  override def pause(): Unit = {}

  override def show(): Unit = {}

  override def resume(): Unit = {}
}
