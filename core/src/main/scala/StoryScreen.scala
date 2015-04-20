package ludumdare32

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.{Texture, GL20, OrthographicCamera}
import com.badlogic.gdx.{Gdx, Screen}

class StoryScreen (game: LudumDareSkeleton) extends Screen {
  lazy val camera = new OrthographicCamera
  camera.setToOrtho(false, 800, 480)
  var state = 0
  lazy val dionysusImage = new Texture(Gdx.files.internal("dionysus.png"))
  lazy val grannyImage = new Texture(Gdx.files.internal("granny.png"))
  lazy val grannyNiceImage = new Texture(Gdx.files.internal("granny_happy.png"))

  lazy val bgMusic = Gdx.audio.newMusic(Gdx.files.internal("bg_granny.wav"))
  //lazy val bgMusic = Gdx.audio.newMusic(Gdx.files.internal("drop.wav"))
  bgMusic.setLooping(true)
  bgMusic.play()

  override def render(delta: Float): Unit = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    camera.update()
    game.batch.setProjectionMatrix(camera.combined)

    val x = 100
    val y = 100
    game.batch.begin()
    if(state == 0) game.font.draw(game.batch, "Once upon a time.", x, y)
    if(state == 1) {
      game.font.draw(game.batch, "There was a woman named Ms. Yphus.", x, y)
    }
    if(state >= 1 && state < 4) {
      game.batch.draw(grannyNiceImage, 200, 200, 83, 205)
    }
    if(state >= 4) {
      game.batch.draw(grannyImage, 200, 200, 83, 205)
    }
    if(state == 2) game.font.draw(game.batch, "She had a cat.", x, y)
    if(state >= 2 && state < 4) {
      game.batch.draw(dionysusImage, 300, 200, 112, 85)
    }
    if(state == 3) game.font.draw(game.batch, "And they loved each other.", x, y)
    if(state == 4) game.font.draw(game.batch, "But one day her cat went missing.", x, y)
    if(state == 5) game.font.draw(game.batch, "A note was left, saying that the person who took her was called Thanatos.", x, y)
    if(state == 6) game.font.draw(game.batch, "He was the leader of a gang known as the Hellenists.", x, y)
    if(state == 7) game.font.draw(game.batch, "Ms. Yphus was determined to get her friend back.", x, y)
    if(state == 8) game.font.draw(game.batch, "She picked up anything she could find to use as a weapon and went to find Thanatos.", x, y)
    if(state == 9) game.font.draw(game.batch, "She knew she had to use WASD to move and space to fire her active weapon.", x, y)
    if(state == 10) game.font.draw(game.batch, "She could select her frying pan with 1 and her iron by pressing 2", x, y)
    if(state == 11) game.font.draw(game.batch, "She could also use her ironing board shield to block bullets by holding j", x, y)
    if(state == 12) game.font.draw(game.batch, "She took a deep breath and went outside", x, y)

    game.font.draw(game.batch, "Press Enter to continue", 100, 30)
    game.batch.end()

    if(Gdx.input.isKeyJustPressed(Keys.ENTER)) {
      state += 1
      if(state == 12) {
        game.setScreen(new GameScreen(game))
        dispose()
      }
    }
  }

  override def resize(width: Int, height: Int): Unit = {}

  override def hide(): Unit = {}

  override def dispose(): Unit = {
    bgMusic.stop()
    bgMusic.dispose()
  }

  override def pause(): Unit = {}

  override def show(): Unit = {}

  override def resume(): Unit = {}
}
