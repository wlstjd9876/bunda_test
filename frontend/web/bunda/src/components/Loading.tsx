import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import { Backdrop, CircularProgress } from "@material-ui/core";

const useStyles = makeStyles((theme) => ({
  backdrop: {
    zIndex: theme.zIndex.drawer + 1,
    color: "#fff",
  },
}));

// const _ = ({ loading, setLoading } : boolean) => {
const _ = () => {
  const classes = useStyles();

  return (
    <Backdrop
      className={classes.backdrop}
      // open={loading}
      open={true}
      onClick={() => {
        // setLoading(false);
        console.log("로딩 해제");
      }}
    >
      <CircularProgress color="inherit" />
    </Backdrop>
  );
};

// const mapState = ({ samples: { loading } }) => ({ loading });
// const mapDispatch = ({ samples: { setLoading } }) => ({ setLoading });

// export default connect(mapState, mapDispatch)(_);
export default _;
