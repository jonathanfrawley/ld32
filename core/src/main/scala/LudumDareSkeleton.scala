package ludumdare32

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.graphics.GL20

class LudumDareSkeleton extends Game {
  lazy val dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"))
  lazy val rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"))
  lazy val dropImage = new Texture(Gdx.files.internal("droplet.png"))
  lazy val bucketImage = new Texture(Gdx.files.internal("bucket.png"))
  lazy val camera = new OrthographicCamera()
  lazy val batch = new SpriteBatch()
  lazy val bucket = new Rectangle()

  override def create() {
    // start the playback of the background music immediately
    rainMusic.setLooping(true)
    rainMusic.play()

    camera.setToOrtho(false, 800, 480);

    bucket.x = 800 / 2 - 64 / 2;
    bucket.y = 20;
    bucket.width = 64;
    bucket.height = 64;
  }

  override def render() {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    camera.update()

    batch.setProjectionMatrix(camera.combined)
    batch.begin()
    batch.draw(bucketImage, bucket.x, bucket.y)
    batch.end()
  }
}
