package ludumdare32
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.{Gdx, Screen}
import com.badlogic.gdx.graphics.g2d.{Animation, BitmapFont, SpriteBatch, TextureRegion}
import com.badlogic.gdx.graphics.{Color, GL20, OrthographicCamera, Texture}
import com.badlogic.gdx.math.{MathUtils, Rectangle, Vector3}
import com.badlogic.gdx.utils.TimeUtils

import scala.collection.mutable.ArrayBuffer

class GameScreen (game: LudumDareSkeleton) extends Screen {
  lazy val dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"))
  lazy val rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"))
  lazy val dropImage = new Texture(Gdx.files.internal("droplet.png"))
  lazy val bucketImage = new Texture(Gdx.files.internal("bucket.png"))
  lazy val camera = new OrthographicCamera()
  lazy val batch = new SpriteBatch()
  var bucket = new Rectangle()
  var raindrops = new ArrayBuffer[Rectangle]()
  var lastDropTime : Long = 0
  //Animation
  val FRAME_COLS = 6
  val FRAME_ROWS = 5
  var walkAnimation : Animation = null
  var walkSheet : Texture = null
  var walkFrames : com.badlogic.gdx.utils.Array[TextureRegion] = null
  var spriteBatch : SpriteBatch = null
  var currentFrame : TextureRegion = null
  var stateTime : Float = 0
  //fonts
  var font : BitmapFont = null

  // start the playback of the background music immediately
  rainMusic.setLooping(true)
  rainMusic.play()

  camera.setToOrtho(false, 800, 480)

  bucket.x = 800 / 2 - 64 / 2
  bucket.y = 20
  bucket.width = 64
  bucket.height = 64

  spawnRaindrop()

  //Animation stuff
  walkSheet = new Texture(Gdx.files.internal("animation_sheet.png"))
  val tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/FRAME_COLS, walkSheet.getHeight()/FRAME_ROWS)
  println("tmp.length " + tmp.length)
  println("tmp2.length " + tmp(0).length)
  walkFrames = new com.badlogic.gdx.utils.Array[TextureRegion](FRAME_COLS * FRAME_ROWS)
  for(i <- 0 to FRAME_ROWS-1) {
    for(j <- 0 to FRAME_COLS-1){
      println("i " + i + " j " + walkFrames.size)
      walkFrames.add(tmp(i)(j))
    }
  }
  walkAnimation = new Animation(0.025f, walkFrames)
  spriteBatch = new SpriteBatch()
  stateTime = 0f

  //fonts
  font = new BitmapFont()
  font.setColor(Color.RED)

  override def render(delta:Float) {
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

    // Animation stuff
    stateTime += Gdx.graphics.getDeltaTime()
    currentFrame = walkAnimation.getKeyFrame(stateTime, true)
    spriteBatch.begin()
    spriteBatch.draw(currentFrame, 50, 50)
    spriteBatch.end()

    // font
    batch.begin()
    font.draw(batch, "Hello World", 200, 200)
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

  override def resize(width: Int, height: Int): Unit = {}

  override def hide(): Unit = {}

  override def pause(): Unit = {}

  override def show(): Unit = {}

  override def resume(): Unit = {}
}
