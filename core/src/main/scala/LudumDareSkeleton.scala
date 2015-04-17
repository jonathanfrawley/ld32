package ludumdare32

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.{BitmapFont, SpriteBatch}

class LudumDareSkeleton extends Game {
  lazy val batch = new SpriteBatch()
  lazy val font = new BitmapFont()

  override def create() {
    setScreen(new MainMenuScreen(this))
  }

  override def render() {
    super.render()
  }

  override def dispose() {
    batch.dispose()
    font.dispose()
  }
}
