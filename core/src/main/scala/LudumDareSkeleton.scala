package ludumdare32

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.{MathUtils, Vector3, Rectangle}
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.utils.TimeUtils

import scala.collection.mutable.ArrayBuffer

class LudumDareSkeleton extends Game {
  lazy val dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"))
  lazy val rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"))
  lazy val dropImage = new Texture(Gdx.files.internal("droplet.png"))
  lazy val bucketImage = new Texture(Gdx.files.internal("bucket.png"))
  lazy val camera = new OrthographicCamera()
  lazy val batch = new SpriteBatch()
  var bucket = new Rectangle()
  var raindrops = new ArrayBuffer[Rectangle]()
  var lastDropTime : Long = 0

  override def create() {
    // start the playback of the background music immediately
    rainMusic.setLooping(true)
    rainMusic.play()

    camera.setToOrtho(false, 800, 480)

    bucket.x = 800 / 2 - 64 / 2
    bucket.y = 20
    bucket.width = 64
    bucket.height = 64

    spawnRaindrop()
  }

  override def render() {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    camera.update()

    batch.setProjectionMatrix(camera.combined)
    batch.begin()
    batch.draw(bucketImage, bucket.x, bucket.y)
    batch.end()

    //Handle input
    if(Gdx.input.isTouched) {
      val touchPos = new Vector3
      touchPos.set(Gdx.input.getX, Gdx.input.getY, 0)
      camera.unproject(touchPos)
      bucket.x = touchPos.x - 64 / 2
    }
    if(Gdx.input.isKeyPressed(Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime
    if(Gdx.input.isKeyPressed(Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime

    // Stay within limits
    if(bucket.x < 0) bucket.x = 0
    if(bucket.x > 800 - 64) bucket.x = 800 - 64

    if(TimeUtils.nanoTime - lastDropTime > 1000000000) spawnRaindrop()

    //TODO: Figure out a nicer way to do this
    var newRainDrops = new ArrayBuffer[Rectangle]()
    for(raindrop <- raindrops) {
      raindrop.y -= 200 * Gdx.graphics.getDeltaTime()
      var remove = false

      if(raindrop.overlaps(bucket)) {
        dropSound.play()
        remove = true
      }

      if(raindrop.y + 64 < 0) remove = true

      if(! remove) newRainDrops += raindrop

    }
    raindrops = newRainDrops

    batch.begin()
    batch.draw(bucketImage, bucket.x, bucket.y)
    for(raindrop <- raindrops) {
      batch.draw(dropImage, raindrop.x, raindrop.y)
    }
    batch.end()

  }

  override def dispose() {
    dropImage.dispose()
    bucketImage.dispose()
    dropSound.dispose()
    rainMusic.dispose()
    batch.dispose()
  }

  def spawnRaindrop() {
    val raindrop = new Rectangle()
    raindrop.x = MathUtils.random(0, 800-64)
    raindrop.y = 480
    raindrop.width = 64
    raindrop.height = 64
    raindrops += raindrop
    lastDropTime = TimeUtils.nanoTime()
  }
}
