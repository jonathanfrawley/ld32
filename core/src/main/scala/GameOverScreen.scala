package ludumdare32

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera}
import com.badlogic.gdx.{Gdx, Screen}

class GameOverScreen (game: LudumDareSkeleton) extends Screen {
  lazy val camera = new OrthographicCamera
  camera.setToOrtho(false, 800, 480)

  override def render(delta: Float): Unit = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    camera.update()
    game.batch.setProjectionMatrix(camera.combined)

    game.batch.begin()
    var x = camera.viewportWidth*0.33f
    var y = 400
    game.font.draw(game.batch, "Game over", x, y)
    y -= 100
    game.font.draw(game.batch, "Instructions:", x, y)
    y -= 20
    game.font.draw(game.batch, "WASD to move, space to fire your active weapon.", x, y)
    y -= 20
    game.font.draw(game.batch, "Select frying pan with 1 and iron by pressing 2", x, y)
    y -= 20
    game.font.draw(game.batch, "Ironing board shield to block bullets by holding j", x, y)
    y -= 100
    game.font.draw(game.batch, "Press enter to retry", x, y)
    game.batch.end()

    if(Gdx.input.isKeyJustPressed(Keys.ENTER)) {
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
