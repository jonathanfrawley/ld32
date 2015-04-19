package ludumdare32
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.utils.Timer.Task

import com.badlogic.gdx.{Gdx, Screen}
import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx.graphics.{Color, GL20, OrthographicCamera, Texture}
import com.badlogic.gdx.math.{MathUtils, Rectangle, Vector3}
import com.badlogic.gdx.utils.{Timer, TimeUtils}

import scala.collection.mutable.ArrayBuffer

object EnemyWeaponType extends Enumeration {
  type EnemyWeaponType = Value
  val Axe, Gun = Value
}
import EnemyWeaponType._

class Iron(var lookingRight:Boolean) {
  var rot = 0.0f
  val rect = new Rectangle()
  rect.width = 20
  rect.height = 10
  var isAttacking = false

  def update(gameScreen: GameScreen): Unit = {
    val ironAngVelocity = 9.0f
    val ironSpeed = 9.0f
    if(lookingRight) {
      if (isAttacking) {
        rot -= ironAngVelocity
        rect.x += ironSpeed
      } else {
        rect.x = gameScreen.granny.x + gameScreen.granny.width * 0.75f
        rect.y = gameScreen.granny.y + gameScreen.granny.height * 0.29f
        rot = 0
      }
    } else {
      if (isAttacking) {
        rot += ironAngVelocity
        rect.x -= ironSpeed
      } else {
        rect.x = gameScreen.granny.x - gameScreen.granny.width * 0.50f
        rect.y = gameScreen.granny.y + gameScreen.granny.height * 0.29f
        rot = 0
      }
    }
  }
}

class Bullet(var lookingRight:Boolean, firerRect: Rectangle) {
  val rect = new Rectangle()
  rect.width = 3
  rect.height = 3
  rect.y = firerRect.y + 0.67f*firerRect.height
  if(lookingRight) {
    rect.x = firerRect.x + 1.7f*firerRect.width
  } else {
    rect.x = firerRect.x - 0.7f*firerRect.width
  }

  def update(): Unit = {
    val bulletSpeed = 9.0f
    if(lookingRight) {
      rect.x += bulletSpeed
    } else {
      rect.x -= bulletSpeed
    }
  }
}

class Minion(val granny:Rectangle, val texture: Texture, val axeTexture: Texture, val gunTexture: Texture, val bulletTexture: Texture, val weaponType:EnemyWeaponType) {
  val rect = new Rectangle()
  rect.width = 30
  rect.height = 60
  val axe = new Rectangle()
  axe.width = 10
  axe.height = 40
  var axeRot = 0.0f
  var axeAttackRot = 0.0f
  val gun = new Rectangle()
  gun.width = 40
  gun.height = 10
  var isAttacking = false
  var allowedToAttack = true
  val axeAngVelocity = 5.0f
  val bullets = new ArrayBuffer[Bullet]()

  def update(gameScreen: GameScreen): Unit = {
    val ySpeed = 0.5f
    val xSpeed = 0.9f
    val smallVal = 1.0f
    val smallValX = 50.0f
    val smallValXGun = gameScreen.gameWidth * 0.4f
    bullets.foreach { case bullet => {
      bullet.update()
    }
    }
    //AI
    if(granny.y - rect.y > smallVal) {
      rect.y += ySpeed
      axeAttackRot = 0.0f
    } else if(granny.y - rect.y < -smallVal) {
      rect.y -= ySpeed
      axeAttackRot = 0.0f
    } else {
      //On same x axis roughly so start moving towards
      if(weaponType == Axe) {
        if(granny.x - rect.x > smallValX) {
          rect.x += xSpeed
          axeAttackRot = 0.0f
        } else if(granny.x - rect.x < -smallValX) {
          rect.x -= xSpeed
          axeAttackRot = 0.0f
        } else {
          if(! isAttacking && allowedToAttack) {
            //Good to attack
            isAttacking = true
            allowedToAttack = false
          } else {
            if(isAttacking) {
              if (facingRight) axeAttackRot -= axeAngVelocity
              else axeAttackRot += axeAngVelocity
              if ((axeAttackRot < -90.0f) || (axeAttackRot > 90.0f)) {
                axeAttackRot = 0.0f
                isAttacking = false
                Timer.schedule(new Task {
                  override def run(): Unit = {
                    allowedToAttack = true
                  }
                }, 1.0f)
              }
            }
          }
        }
      } else {
        if(granny.x - rect.x > smallValXGun) {
          rect.x += xSpeed
        } else if(granny.x - rect.x < -smallValXGun) {
          rect.x -= xSpeed
        } else {
          if(! isAttacking && allowedToAttack) {
            //Good to attack
            isAttacking = true
            allowedToAttack = false
            //Spawn bullet
            spawnBullet()

            Timer.schedule(new Task {
              override def run(): Unit = {
                allowedToAttack = true
                isAttacking = false
              }
            }, 1.0f)
          }
        }
      }
    }

    if (weaponType == Axe) {
      axe.y = rect.y + rect.height * 0.6f
      if (facingRight) {
        axe.x = rect.x + rect.width * 0.75f
      } else {
        axe.x = rect.x - rect.width * 0.1f
      }
    } else {
      gun.y = rect.y + rect.height * 0.6f
      if (facingRight) {
        gun.x = rect.x + rect.width * 0.35f
      } else {
        gun.x = rect.x - rect.width * 0.55f
      }
    }
  }

  def spawnBullet() {
    val bullet = new Bullet(facingRight, rect)
    bullets += bullet
  }

  def facingRight(): Boolean = (rect.x < granny.x)

  def render(gameScreen: GameScreen): Unit = {
    gameScreen.game.batch.draw(texture, rect.x, rect.y, rect.width, rect.height, 0, 0, texture.getWidth, texture.getHeight, facingRight, false)
    if(weaponType == Axe) {
      gameScreen.game.batch.draw(axeTexture, axe.x, axe.y, axe.width * 0.5f, axe.height * 0.1f, axe.width, axe.height, 1.0f, 1.0f, axeRot + axeAttackRot, 0, 0, axeTexture.getWidth, axeTexture.getHeight, false, false)
    } else {
      gameScreen.game.batch.draw(gunTexture, gun.x, gun.y, gun.height * 0.1f, gun.height / 2, gun.width, gun.height, 1.0f, 1.0f, 0, 0, 0, gunTexture.getWidth, gunTexture.getHeight, facingRight, false)
    }
    bullets.foreach { case bullet => gameScreen.game.batch.draw(bulletTexture, bullet.rect.x, bullet.rect.y, bullet.rect.width / 2, bullet.rect.height / 2, bullet.rect.width, bullet.rect.height, 1.0f, 1.0f, 0.0f, 0, 0, bulletTexture.getWidth, bulletTexture.getHeight, bullet.lookingRight, false) }
  }
}

class GameScreen (val game: LudumDareSkeleton) extends Screen {

  object WeaponType extends Enumeration {
    type WeaponType = Value
    val FryingPan, Iron = Value
  }
  import WeaponType._

  /*
  lazy val dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"))
  lazy val rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"))
  lazy val dropImage = new Texture(Gdx.files.internal("droplet.png"))
  */
  lazy val grannyImage = new Texture(Gdx.files.internal("granny.png"))
  lazy val fryingPanImage = new Texture(Gdx.files.internal("frying_pan.png"))

  lazy val ironImage = new Texture(Gdx.files.internal("iron.png"))
  lazy val ironingBoardImage = new Texture(Gdx.files.internal("ironing_board.png"))

  lazy val backgroundImage = new Texture(Gdx.files.internal("background_granny.png"))

  lazy val camera = new OrthographicCamera()
  lazy val game.batch = new SpriteBatch()
  //lazy var granny = new Sprite(grannyImage)
  lazy val granny = new Rectangle()
  //Weapons
  var weaponType = FryingPan
  var isAttacking = false
  lazy val fryingPan = new Rectangle()
  var fryingPanRot = 90.0f
  //lazy val activeIron = new Rectangle()
  //var activeIronRot = 0.0f
  var activeIron = new Iron(false)
  var irons = new ArrayBuffer[Iron]()
  // Shield
  var isDefending = false
  lazy val ironingBoard = new Rectangle()

  lazy val background0 = new Rectangle()
  lazy val background1 = new Rectangle()
  lazy val background2 = new Rectangle()

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
  var lookingRight : Boolean = false
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

  fryingPan.width = 32
  fryingPan.height = 16

  ironingBoard.width = 20
  ironingBoard.height = 80

  background0.width = gameWidth
  background0.height = gameHeight
  background1.width = gameWidth
  background1.height = gameHeight
  background2.width = gameWidth
  background2.height = gameHeight

  //Bad guys... boo hiss
  lazy val thanatosImage = new Texture(Gdx.files.internal("thanatos.png"))
  lazy val minionImage = new Texture(Gdx.files.internal("minion.png"))
  lazy val gunImage = new Texture(Gdx.files.internal("gun.png"))
  lazy val bulletImage = new Texture(Gdx.files.internal("bullet.png"))
  lazy val axeImage = new Texture(Gdx.files.internal("axe.png"))
  var minions = new ArrayBuffer[Minion]()
  lazy val thanatos = new Rectangle()
  thanatos.x = 500
  thanatos.y = gameHeight * 0.33f
  thanatos.width = 50
  thanatos.height = 100

  Timer.schedule(new Task {
    override def run(): Unit = {
      spawnMinion()
    }
  }, 1.0f)

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
  var scheduled = false

  def fryingPanUpdate(): Unit = {
    if(weaponType == FryingPan) {
      val fryingPanSpeed = 9.0f
      if (lookingRight) {
        fryingPan.x = granny.x + granny.width * 0.95f
        fryingPan.y = granny.y + granny.height * 0.29f
        if (isAttacking) {
          fryingPanRot -= fryingPanSpeed
          if (fryingPanRot < 0.0f) {
            fryingPanRot = 90.0f
            isAttacking = false
          }
        } else {
          fryingPanRot = 90.0f
        }
      } else {
        fryingPan.x = granny.x - granny.width * 0.10f
        fryingPan.y = granny.y + granny.height * 0.29f
        if (isAttacking) {
          fryingPanRot += fryingPanSpeed
          if (fryingPanRot > 180.0f) {
            fryingPanRot = 90.0f
            isAttacking = false
          }
        } else {
          fryingPanRot = 90.0f
        }
      }
    }
  }

  def ironUpdate(): Unit = {
    if(weaponType == Iron) {
      if (activeIron != null && isAttacking) {
        activeIron.isAttacking = true
        irons += activeIron
        activeIron = null
        scheduled = true
        Timer.schedule(new Task {
          override def run(): Unit = {
            isAttacking = false
            scheduled = false
          }
        }, 1.0f)
      } else {
        if (activeIron == null && !isAttacking) {
          activeIron = new Iron(lookingRight)
        }/* else {
          if(activeIron==null && isAttacking && !scheduled) {
            activeIron = new Iron(lookingRight)
          }
        }*/
      }
    }
    if(activeIron != null) {
      activeIron.update(this)
      activeIron.lookingRight = lookingRight
    }
    irons.foreach { case iron => {
        iron.update(this)
      }
    }
  }

  def ironingBoardUpdate(): Unit = {
    ironingBoard.y = granny.y + granny.height * 0.0f
    if(lookingRight) {
      ironingBoard.x = granny.x + granny.width * 0.75f
    } else {
      ironingBoard.x = granny.x - granny.width * 0.50f
    }
  }

  override def render(delta:Float) {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    val lerp = 0.05f
    var diff = (granny.x - camera.position.x) * lerp
    val smallVal = 0.1
    if(diff < smallVal && diff > -smallVal) diff = 0.0f
    camera.position.set(camera.position.x + diff, camera.position.y, 0)
    camera.update()

    //Infinite scrolling logic
    if(!background1.overlaps(granny)) {
      if(granny.x > background1.x) {
        background1.x += background1.width
      } else {
        background1.x -= background1.width
      }
    }

    background0.x = background1.x - background1.width
    background2.x = background1.x + background1.width

    fryingPanUpdate
    ironUpdate
    ironingBoardUpdate
    minions.foreach { case minion => minion.update(this) }

    game.batch.setProjectionMatrix(camera.combined)
    game.batch.begin()
    game.batch.draw(backgroundImage, background0.x, background0.y, background0.width, background0.height)
    game.batch.draw(backgroundImage, background1.x, background1.y, background1.width, background1.height)
    game.batch.draw(backgroundImage, background2.x, background2.y, background2.width, background2.height)
    //game.batch.draw(grannyImage, granny.x, granny.y)
    game.batch.draw(grannyImage, granny.x, granny.y, granny.width, granny.height)
    //game.batch.draw(fryingPanImage, fryingPan.x, fryingPan.y, fryingPan.width, fryingPan.height)
    if(isDefending) {
      game.batch.draw(ironingBoardImage, ironingBoard.x, ironingBoard.y, ironingBoard.height/2, ironingBoard.height/2, ironingBoard.width, ironingBoard.height, 1.0f, 1.0f, 0, 0, 0, ironingBoardImage.getWidth, ironingBoardImage.getHeight, false, false)
    } else {
      if(weaponType == FryingPan) game.batch.draw(fryingPanImage, fryingPan.x, fryingPan.y, fryingPan.height*0.1f, fryingPan.height/2, fryingPan.width, fryingPan.height, 1.0f, 1.0f, fryingPanRot, 0, 0, fryingPanImage.getWidth, fryingPanImage.getHeight, false, false)
      if(weaponType == Iron && activeIron != null) game.batch.draw(ironImage, activeIron.rect.x, activeIron.rect.y, activeIron.rect.width/2, activeIron.rect.height/2, activeIron.rect.width, activeIron.rect.height, 1.0f, 1.0f, activeIron.rot, 0, 0, ironImage.getWidth, ironImage.getHeight, lookingRight, false)
    }

    irons.foreach { case iron => game.batch.draw(ironImage, iron.rect.x, iron.rect.y, iron.rect.width / 2, iron.rect.height / 2, iron.rect.width, iron.rect.height, 1.0f, 1.0f, iron.rot, 0, 0, ironImage.getWidth, ironImage.getHeight, iron.lookingRight, false) }
    minions.foreach { case minion => minion.render(this) }

    game.batch.draw(thanatosImage, thanatos.x, thanatos.y, thanatos.width, thanatos.height)

    game.batch.end()

    //Handle input
    if(Gdx.input.isKeyPressed(Keys.NUM_1)) weaponType = FryingPan
    if(Gdx.input.isKeyPressed(Keys.NUM_2)) weaponType = Iron
    isDefending = Gdx.input.isKeyPressed(Keys.J)
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
    if(Gdx.input.isKeyPressed(Keys.A)) { granny.x -= grannyXSpeed * Gdx.graphics.getDeltaTime; lookingRight = false }
    if(Gdx.input.isKeyPressed(Keys.D)) { granny.x += grannyXSpeed * Gdx.graphics.getDeltaTime; lookingRight = true }
    if(Gdx.input.isKeyPressed(Keys.S)) granny.y -= grannyYSpeed * Gdx.graphics.getDeltaTime
    if(Gdx.input.isKeyPressed(Keys.W)) granny.y += grannyYSpeed * Gdx.graphics.getDeltaTime
    if(!isDefending && !isAttacking && Gdx.input.isKeyPressed(Keys.SPACE)) isAttacking = true

    // Stay within limits
    val limitPct = 0.75f
    if(granny.x < 0) granny.x = 0
    //if(granny.x > gameWidth - granny.width) granny.x = gameWidth - granny.width
    if(granny.y < 0) granny.y = 0
    if(granny.y > (gameHeight * limitPct) - granny.height) granny.y = (gameHeight * limitPct) - granny.height

    //background.x = 300 - granny.x;

    //camera.lookAt(granny.x, granny.y, 0)
    //camera.translate(0.1f, 0.0f, 0.0f)

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

  def spawnMinion() {
    val minion = new Minion(granny, minionImage, axeImage, gunImage, bulletImage, Gun)
    //val minion = new Minion(granny, minionImage, axeImage, gunImage, bulletImage, Axe)
    minions += minion

    Timer.schedule(new Task {
      override def run(): Unit = {
        spawnMinion()
      }
    }, 10.0f)
  }

  override def resize(width: Int, height: Int): Unit = {}

  override def hide(): Unit = {}

  override def pause(): Unit = {}

  override def show(): Unit = {}

  override def resume(): Unit = {}
}
