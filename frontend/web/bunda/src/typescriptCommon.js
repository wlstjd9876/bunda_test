export interface PlatformInterface {
  platform: {
    os: {
      family: string,
    },
    ua?: string,
  };
}
