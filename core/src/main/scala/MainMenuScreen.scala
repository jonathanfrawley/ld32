
package ludumdare32

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.{Texture, GL20, OrthographicCamera}
import com.badlogic.gdx.{Gdx, Screen}

class MainMenuScreen (game: LudumDareSkeleton) extends Screen {
  lazy val camera = new OrthographicCamera
  camera.setToOrtho(false, 800, 480)
  //lazy val backgroundImage = new Texture(Gdx.files.internal("credits.png"))

  lazy val grannyNiceImage = new Texture(Gdx.files.internal("granny_happy.png"))
  lazy val dionysusImage = new Texture(Gdx.files.internal("dionysus.png"))

  override def render(delta: Float): Unit = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    camera.update()
    game.batch.setProjectionMatrix(camera.combined)

    game.batch.begin()
    var x = camera.viewportWidth*0.33f
    var y = 400
    game.font.draw(game.batch, "Thanatos and the cat god", x, y)
    y -= 30
    game.font.draw(game.batch, "Made by Jonathan Frawley @df3n5", x, y)
    y -= 200
    game.batch.draw(dionysusImage, x, y, 112, 85)
    y -= 30
    game.font.draw(game.batch, "Press enter to start", x, y)

    game.batch.end()

    if(Gdx.input.isKeyJustPressed(Keys.ENTER)) {
      game.setScreen(new StoryScreen(game))
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

