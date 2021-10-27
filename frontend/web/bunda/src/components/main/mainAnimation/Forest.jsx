/* eslint-disable no-unused-vars */
import React, { useRef } from "react";
import { Canvas } from "@react-three/fiber";
import { Environment, OrbitControls, Sphere } from "@react-three/drei";

const _ = () => {
  return (
    <>
      <Canvas>
        {/* <ambientLight intensity={0.5} />
        <spotLight position={[10, 20, 10]} angle={0.3} /> */}
        <Environment preset="forest" background />
        <Bodys position={[0, 0, 0]} />
        <TorusBodys position={[1, 1, 1]} />
        <SphereBodys position={[-1, -1, -1]} />
      </Canvas>
    </>
  );
};

export default _;

const Bodys = (props) => {
  const mesh = useRef();
  return (
    <mesh {...props} ref={mesh} scale={0.5}>
      <OrbitControls />
      {/* <boxBufferGeometry attach="geometry" /> */}
      <torusKnotBufferGeometry args={[1, 0.4, 128, 32]} />
      <meshStandardMaterial metalness={1} roughness={0} />
    </mesh>
  );
};

const TorusBodys = (props) => {
  const mesh = useRef();
  return (
    <mesh {...props} ref={mesh} scale={0.5}>
      <OrbitControls />

      <torusGeometry args={[0.8, 0.2, 16, 100]} />
      <meshStandardMaterial metalness={1} roughness={0} />
    </mesh>
  );
};

const SphereBodys = (props) => {
  const mesh = useRef();
  return (
    <mesh {...props} ref={mesh} scale={0.5}>
      <OrbitControls />
      <sphereGeometry />
      <meshStandardMaterial metalness={1} roughness={0} />
    </mesh>
  );
};
