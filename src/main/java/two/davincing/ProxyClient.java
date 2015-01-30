/*
 */
package two.davincing;

import two.davincing.item.CanvasRenderer;
import two.davincing.item.DroppedSculptureRenderer;
import two.davincing.item.PieceRenderer;
import two.davincing.painting.PaintingRenderer;
import two.davincing.sculpture.SculptureEntityRenderer;
import two.davincing.sculpture.SculptureRender;

/**
 * This class will only be loaded if this is running in client mode.
 * All client-only initialization (rendering, sound, ...) goes here.
 * 
 * @author Two
 */
public class ProxyClient extends ProxyBase {

  @Override
  protected void registerRenderers() {
    ProxyBase.blockSculpture.registerRendering(new SculptureRender(), new SculptureEntityRenderer());
    ProxyBase.blockPainting.registerRendering(null, new PaintingRenderer());

    ProxyBase.itemPiece.registerRendering(new PieceRenderer());
    ProxyBase.itemBar.registerRendering(new PieceRenderer.Bar());
    ProxyBase.itemCover.registerRendering(new PieceRenderer.Cover());
    ProxyBase.itemDroppedSculpture.registerRendering(new DroppedSculptureRenderer());
    ProxyBase.itemCanvas.registerRendering(new CanvasRenderer());
  }

}
