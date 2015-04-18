package ludumdare32
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.{Gdx, Screen}
import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx.graphics.{Color, GL20, OrthographicCamera, Texture}
import com.badlogic.gdx.math.{MathUtils, Rectangle, Vector3}
import com.badlogic.gdx.utils.TimeUtils

import scala.collection.mutable.ArrayBuffer

class GameScreen (game: LudumDareSkeleton) extends Screen {
  /*
  lazy val dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"))
  lazy val rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"))
  lazy val dropImage = new Texture(Gdx.files.internal("droplet.png"))
  */
  lazy val grannyImage = new Texture(Gdx.files.internal("granny.png"))
  lazy val camera = new OrthographicCamera()
  lazy val game.batch = new SpriteBatch()
  //lazy var granny = new Sprite(grannyImage)
  lazy val granny = new Rectangle()
  //var raindrops = new ArrayBuffer[Rectangle]()
  //var lastDropTime : Long = 0
  //Animation
  val FRAME_COLS = 6
  val FRAME_ROWS = 5
  /*
  var walkAnimation : Animation = null
  var walkSheet : Texture = null
  var walkFrames : com.badlogic.gdx.utils.Array[TextureRegion] = null
  var spriteBatch : SpriteBatch = null
  */
  var currentFrame : TextureRegion = null
  var stateTime : Float = 0
  //fonts
  var font : BitmapFont = null

  /*
  // start the playback of the background music immediately
  rainMusic.setLooping(true)
  rainMusic.play()
  */
  val gameHeight = 480
  val gameWidth = 800

  camera.setToOrtho(false, gameWidth, gameHeight)

  granny.width = 27
  granny.height = 51
  granny.x = gameWidth / 2 - granny.width / 2
  granny.y = 20

  //spawnRaindrop()

  /*
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
  */

  //fonts
  font = new BitmapFont()
  font.setColor(Color.RED)

  override def render(delta:Float) {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    camera.update()

    game.batch.setProjectionMatrix(camera.combined)
    game.batch.begin()
    //game.batch.draw(grannyImage, granny.x, granny.y)
    game.batch.draw(grannyImage, granny.x, granny.y, granny.width, granny.height)
    game.batch.end()

    //Handle input
    /*
    if(Gdx.input.isTouched) {
      val touchPos = new Vector3
      touchPos.set(Gdx.input.getX, Gdx.input.getY, 0)
      camera.unproject(touchPos)
      granny.x = touchPos.x - 64 / 2
    }
    */
    val grannyXSpeed = 300
    val grannyYSpeed = 200
    if(Gdx.input.isKeyPressed(Keys.A)) granny.x -= grannyXSpeed * Gdx.graphics.getDeltaTime
    if(Gdx.input.isKeyPressed(Keys.D)) granny.x += grannyXSpeed * Gdx.graphics.getDeltaTime
    if(Gdx.input.isKeyPressed(Keys.S)) granny.y -= grannyYSpeed * Gdx.graphics.getDeltaTime
    if(Gdx.input.isKeyPressed(Keys.W)) granny.y += grannyYSpeed * Gdx.graphics.getDeltaTime

    // Stay within limits
    if(granny.x < 0) granny.x = 0
    if(granny.x > gameWidth - granny.width)  granny.x = gameWidth - granny.width
    if(granny.y < 0) granny.y = 0
    if(granny.y > gameHeight - granny.height) granny.y = gameHeight - granny.height

    //if(TimeUtils.nanoTime - lastDropTime > 1000000000) spawnRaindrop()

    /*
    //TODO: Figure out a nicer way to do this
    var newRainDrops = new ArrayBuffer[Rectangle]()
    for(raindrop <- raindrops) {
      raindrop.y -= 200 * Gdx.graphics.getDeltaTime()
      var remove = false

      if(raindrop.overlaps(granny)) {
        dropSound.play()
        remove = true
      }

      if(raindrop.y + 64 < 0) remove = true

      if(! remove) newRainDrops += raindrop

    }
    raindrops = newRainDrops
    */

    /*
    game.batch.begin()
    game.batch.draw(grannyImage, granny.x, granny.y)
    for(raindrop <- raindrops) {
      batch.draw(dropImage, raindrop.x, raindrop.y)
    }
    game.batch.end()
    */

    /*
    // Animation stuff
    stateTime += Gdx.graphics.getDeltaTime()
    currentFrame = walkAnimation.getKeyFrame(stateTime, true)
    spriteBatch.begin()
    spriteBatch.draw(currentFrame, 50, 50)
    spriteBatch.end()
    */

    // font
    /*
    game.batch.begin()
    font.draw(game.batch, "Hello World", 200, 200)
    game.batch.end()
    */

  }

  override def dispose() {
    grannyImage.dispose()
    /*
    dropImage.dispose()
    dropSound.dispose()
    rainMusic.dispose()
    batch.dispose()
    */
  }

  def spawnRaindrop() {
    /*
    val raindrop = new Rectangle()
    raindrop.x = MathUtils.random(0, gameWidth-64)
    raindrop.y = gameHeight
    raindrop.width = 64
    raindrop.height = 64
    raindrops += raindrop
    lastDropTime = TimeUtils.nanoTime()
    */
  }

  override def resize(width: Int, height: Int): Unit = {}

  override def hide(): Unit = {}

  override def pause(): Unit = {}

  override def show(): Unit = {}

  override def resume(): Unit = {}
}
