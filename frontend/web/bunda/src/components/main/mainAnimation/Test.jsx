/* eslint-disable no-unused-vars */
import React, { useState, useCallback, useRef } from "react";
import { Canvas, useFrame } from "@react-three/fiber";
import { OrbitControls, useTexture, RoundedBox } from "@react-three/drei";
import FrontIphoneImage from "../../../images/front_iphone.png";
import BackIphoneImage from "../../../images/back_iphone.png";

const _ = () => {
  const [open, setOpen] = useState(true);

  const openClickAction = useCallback(() => {
    open ? setOpen(false) : setOpen(true);
  }, [open]);

  return (
    <>
      <Canvas>
        <ambientLight intensity={0.5} />
        <spotLight position={[10, 20, 10]} angle={0.3} />
        <OrbitControls />
        <TopBody
          openClick={{ openClickAction }}
          open={open}
          position={[0, -0.7, 0]}
        />
        <BottomBody
          openClick={{ openClickAction }}
          open={open}
          position={[0, 0.7, 0]}
        />
        <FrontIphone position={[0, 0, 0.01]} />
        <CenterIphone position={[0, 0, 0]} />
        <BackIphone position={[0, 0, -0.01]} rotation={[Math.PI, 0, 3.14]} />
      </Canvas>
    </>
  );
};

export default _;

const TopBody = (props) => {
  const mesh = useRef();

  useFrame((state, delta) => {
    mesh.current.position.y = props.open ? -1.5 : -0.7;
  });

  return (
    <mesh
      {...props}
      ref={mesh}
      scale={1.5}
      onClick={(event) => props.openClick.openClickAction()}
    >
      <boxBufferGeometry attach="geometry" />
      <meshNormalMaterial attach="material" color="hotpink" />
    </mesh>
  );
};

const BottomBody = (props) => {
  const mesh = useRef();

  useFrame((state, delta) => {
    mesh.current.position.y = props.open ? 1.5 : 0.7;
  });

  return (
    <mesh
      {...props}
      ref={mesh}
      scale={1.5}
      onClick={(event) => props.openClick.openClickAction()}
    >
      <boxBufferGeometry attach="geometry" />
      <meshNormalMaterial attach="material" color="hotpink" />
    </mesh>
  );
};

const FrontIphone = (props) => {
  const a = useTexture({
    map: FrontIphoneImage,
  });
  return (
    <mesh {...props}>
      <planeGeometry args={[0.52, 1]} />
      <meshStandardMaterial {...a} />
    </mesh>
  );
};

const CenterIphone = (props) => {
  return (
    <mesh {...props}>
      <RoundedBox args={[0.52, 1, 0.019]} radius={0.009}>
        <meshStandardMaterial attach="material" />
      </RoundedBox>
    </mesh>
  );
};

const BackIphone = (props) => {
  const a = useTexture({
    map: BackIphoneImage,
  });
  return (
    <mesh {...props}>
      <planeGeometry args={[0.52, 1]} />
      <meshStandardMaterial {...a} />
    </mesh>
  );
};
