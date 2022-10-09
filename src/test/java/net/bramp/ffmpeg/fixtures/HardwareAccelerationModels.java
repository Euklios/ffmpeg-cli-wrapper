package net.bramp.ffmpeg.fixtures;

import com.google.common.collect.ImmutableList;
import net.bramp.ffmpeg.info.HardwareAccelerationModel;
import net.bramp.ffmpeg.info.Protocol;

public class HardwareAccelerationModels {
  private HardwareAccelerationModels() {
    throw new AssertionError("No instances for you!");
  }

  public static final ImmutableList<HardwareAccelerationModel> HARDWARE_ACCELERATION_MODELS =
      new ImmutableList.Builder<HardwareAccelerationModel>()
          .add(
              new HardwareAccelerationModel("cuda"),
              new HardwareAccelerationModel("dxva2"),
              new HardwareAccelerationModel("qsv"),
              new HardwareAccelerationModel("d3d11va"),
              new HardwareAccelerationModel("opencl"),
              new HardwareAccelerationModel("vulkan")
          ).build();
}
