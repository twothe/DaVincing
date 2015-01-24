/*   1:    */ package hx.minepainter.item;
/*   2:    */ 
/*   3:    */ import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
/*   4:    */ import hx.minepainter.ModMinePainter;
/*   5:    */ import hx.minepainter.sculpture.Operations;
/*   6:    */ import hx.minepainter.sculpture.SculptureOperationMessage;
/*   7:    */ import net.minecraft.block.Block;
/*   8:    */ import net.minecraft.block.Block.SoundType;
/*   9:    */ import net.minecraft.entity.player.EntityPlayer;
/*  10:    */ import net.minecraft.init.Blocks;
/*  11:    */ import net.minecraft.item.Item;
/*  12:    */ import net.minecraft.item.ItemStack;
/*  13:    */ import net.minecraft.server.MinecraftServer;
/*  14:    */ import net.minecraft.world.World;
/*  15:    */ 
/*  16:    */ public class ChiselItem
/*  17:    */   extends Item
/*  18:    */ {
/*  19:    */   public ChiselItem()
/*  20:    */   {
/*  21: 27 */     func_77637_a(ModMinePainter.tabMinePainter);
/*  22: 28 */     func_77655_b("chisel");
/*  23: 29 */     func_111206_d("minepainter:stone_chisel");
/*  24: 30 */     func_77625_d(1);
/*  25: 31 */     func_77656_e(240);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public boolean func_77648_a(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs)
/*  29:    */   {
/*  30: 36 */     if (!w.field_72995_K) {
/*  31: 36 */       return false;
/*  32:    */     }
/*  33: 37 */     int[] pos = Operations.raytrace(x, y, z, ep);
/*  34:    */     
/*  35: 39 */     int flags = getChiselFlags(ep);
/*  36: 40 */     Block editBlock = getEditBlock(is);
/*  37: 41 */     int editMeta = getEditMeta(is);
/*  38: 42 */     if (!Operations.validOperation(w, x, y, z, pos, flags)) {
/*  39: 43 */       return false;
/*  40:    */     }
/*  41: 45 */     if (MinecraftServer.func_71276_C() == null)
/*  42:    */     {
/*  43: 46 */       boolean done = Operations.applyOperation(w, x, y, z, pos, flags, editBlock, editMeta);
/*  44: 47 */       if (!done) {
/*  45: 47 */         return false;
/*  46:    */       }
/*  47:    */     }
/*  48: 50 */     ModMinePainter.network.sendToServer(new SculptureOperationMessage(pos, x, y, z, editBlock, editMeta, flags));
/*  49:    */     
/*  50: 52 */     w.func_72908_a(x + 0.5D, y + 0.5D, z + 0.5D, getEditBlock(is).field_149762_H.field_150501_a, 0.5F, 0.5F);
/*  51:    */     
/*  52: 54 */     return true;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public Block getEditBlock(ItemStack is)
/*  56:    */   {
/*  57: 58 */     return Blocks.field_150350_a;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public int getEditMeta(ItemStack is)
/*  61:    */   {
/*  62: 62 */     return 0;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public int getChiselFlags(EntityPlayer ep)
/*  66:    */   {
/*  67: 66 */     return 16;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static class Saw
/*  71:    */     extends ChiselItem
/*  72:    */   {
/*  73:    */     public Saw()
/*  74:    */     {
/*  75: 73 */       func_77655_b("saw");
/*  76: 74 */       func_111206_d("minepainter:diamond_chisel");
/*  77:    */     }
/*  78:    */     
/*  79:    */     public int getChiselFlags(EntityPlayer ep)
/*  80:    */     {
/*  81: 79 */       int axis = Operations.getLookingAxis(ep);
/*  82: 80 */       switch (axis)
/*  83:    */       {
/*  84:    */       case 0: 
/*  85: 81 */         return 28;
/*  86:    */       case 1: 
/*  87: 82 */         return 26;
/*  88:    */       case 2: 
/*  89: 83 */         return 22;
/*  90:    */       }
/*  91: 85 */       return 0;
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   public static class Barcutter
/*  96:    */     extends ChiselItem
/*  97:    */   {
/*  98:    */     public Barcutter()
/*  99:    */     {
/* 100: 93 */       func_77655_b("barcutter");
/* 101: 94 */       func_111206_d("minepainter:iron_chisel");
/* 102:    */     }
/* 103:    */     
/* 104:    */     public int getChiselFlags(EntityPlayer ep)
/* 105:    */     {
/* 106: 99 */       int axis = Operations.getLookingAxis(ep);
/* 107:100 */       switch (axis)
/* 108:    */       {
/* 109:    */       case 0: 
/* 110:101 */         return 18;
/* 111:    */       case 1: 
/* 112:102 */         return 20;
/* 113:    */       case 2: 
/* 114:103 */         return 24;
/* 115:    */       }
/* 116:105 */       return 0;
/* 117:    */     }
/* 118:    */   }
/* 119:    */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.item.ChiselItem
 * JD-Core Version:    0.7.0.1
 */